package com.safecircleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyCircleActivity : AppCompatActivity() {

    private lateinit var circleMembersAdapter: CircleMemberAdapter
    private lateinit var circleMembersList: MutableList<CircleMember>
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var userReference: DatabaseReference
    private lateinit var imgbtnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_circle)

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance()
        userReference = FirebaseDatabase.getInstance().getReference("Users")

        recyclerView = findViewById(R.id.recyclerview)
        imgbtnBack = findViewById(R.id.imgbtnBack)
        circleMembersList = mutableListOf()
        circleMembersAdapter = CircleMemberAdapter(this, circleMembersList)
        recyclerView.adapter = circleMembersAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Call retrieveCircleMembers() to fetch and display the data
        retrieveCircleMembers()

        imgbtnBack.setOnClickListener {
            // Navigate back to NavigationActivity
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun retrieveCircleMembers() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        // Reference to the circle members under the current user's node
        val circleMembersReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(currentUser.uid)
            .child("CircleMembers")

        // Use addListenerForSingleValueEvent to fetch the data once
        circleMembersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before adding new members
                circleMembersList.clear()

                // Temporary list to store the members
                val tasks = mutableListOf<CircleMember>()

                // Iterate over the circle members, where each member is a user ID
                for (circleMemberSnapshot in snapshot.children) {
                    val circleMemberId = circleMemberSnapshot.key ?: continue
                    Log.d("CircleMemberID", "Found member ID: $circleMemberId")

                    // Fetch the circle member's details
                    val memberRef = FirebaseDatabase.getInstance().getReference("Users").child(circleMemberId)
                    memberRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(memberSnapshot: DataSnapshot) {
                            if (memberSnapshot.exists()) {
                                val name = memberSnapshot.child("name").getValue(String::class.java) ?: "Unknown"
                                val email = memberSnapshot.child("email").getValue(String::class.java) ?: "No Email"
                                val profilePictureUrl = memberSnapshot.child("imageUrl").getValue(String::class.java) ?: ""
                                val isSharing = memberSnapshot.child("sharing").getValue(String::class.java) ?: "false"

                                // Create a CircleMember object and add it to the temporary list
                                val circleMember = CircleMember(name, email, profilePictureUrl, isSharing)
                                tasks.add(circleMember)

                                // Check if all members have been fetched
                                if (tasks.size == snapshot.childrenCount.toInt()) {
                                    circleMembersList.addAll(tasks) // Add all fetched members to the main list
                                    circleMembersAdapter.notifyDataSetChanged() // Notify adapter
                                }
                            } else {
                                Log.d("CircleMember", "Member does not exist: $circleMemberId")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FirebaseError", "Error fetching member details: ${error.message}")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching circle members: ${error.message}")
            }
        })
    }
}