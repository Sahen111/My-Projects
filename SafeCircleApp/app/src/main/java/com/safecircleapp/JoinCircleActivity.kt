package com.safecircleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.goodiebag.pinview.Pinview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JoinCircleActivity : AppCompatActivity() {

    private lateinit var pinView: Pinview
    private lateinit var reference: DatabaseReference
    private lateinit var currentReference: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private var joinUserId: String = ""
    private lateinit var circleReference: DatabaseReference
    private lateinit var imgbtnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_circle)

        pinView = findViewById(R.id.pinview)
        imgbtnBack = findViewById(R.id.imgbtnBack)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        reference = FirebaseDatabase.getInstance().getReference("Users")
        currentReference = FirebaseDatabase.getInstance().getReference("Users").child(user.uid)

        currentUserId = user.uid

        imgbtnBack.setOnClickListener {
            // Navigate back to NavigationActivity
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun JoinCircleButton(view: View) {
        val pin = pinView.value

        val query = reference.orderByChild("code").equalTo(pin)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var createUser: CreateUser? = null
                    for (childDss in dataSnapshot.children) {
                        createUser = childDss.getValue(CreateUser::class.java)
                    }

                    createUser?.let {
                        joinUserId = it.userId
                        val circleReference = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(joinUserId)
                            .child("CircleMembers")

                        // Retrieve current user's name, latitude, and longitude
                        currentReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(currentSnapshot: DataSnapshot) {
                                val userName = currentSnapshot.child("name").getValue(String::class.java) ?: ""
                                val userLatitude = currentSnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
                                val userLongitude = currentSnapshot.child("longitude").getValue(Double::class.java) ?: 0.0

                                // Create a CircleJoin object with the additional information
                                val circleJoin = CircleJoin(
                                    circleMemberId = currentUserId,
                                    name = userName,
                                    latitude = userLatitude,
                                    longitude = userLongitude
                                )

                                // Add the user to the circle
                                circleReference.child(user.uid).setValue(circleJoin)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(applicationContext, "Successfully joined circle", Toast.LENGTH_SHORT).show()
                                            // Navigate to NavigationActivity
                                            val intent = Intent(this@JoinCircleActivity, NavigationActivity::class.java)
                                            startActivity(intent)
                                            finish()  // Close the current activity
                                        } else {
                                            Toast.makeText(applicationContext, "Failed to join circle", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Toast.makeText(applicationContext, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    // Show a toast message if the code is incorrect
                    Toast.makeText(applicationContext, "Incorrect code entered", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}