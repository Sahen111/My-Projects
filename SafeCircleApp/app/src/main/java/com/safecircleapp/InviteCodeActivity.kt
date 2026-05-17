package com.safecircleapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class InviteCodeActivity : AppCompatActivity() {

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var date: String
    private lateinit var isSharing: String
    private lateinit var code: String
    private lateinit var imageUrl: String

    private lateinit var progressDialog: ProgressDialog
    private lateinit var txtCode: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_code)

        txtCode = findViewById(R.id.textView)

        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)

        val myIntent: Intent = intent

        reference = FirebaseDatabase.getInstance().getReference("Users")

        if (myIntent != null) {
            name = myIntent.getStringExtra("name") ?: ""
            email = myIntent.getStringExtra("email") ?: ""
            password = myIntent.getStringExtra("password") ?: ""
            code = myIntent.getStringExtra("code") ?: ""
            isSharing = myIntent.getStringExtra("isSharing") ?: ""
            imageUrl = myIntent.getStringExtra("imageUrl") ?: ""
        }

        // Set the invite code in the TextView
        txtCode.text = code

        findViewById<View>(R.id.btnRegister).setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        progressDialog.setMessage("Please wait while we create your account...")
        progressDialog.show()

        // Check if user is already signed in
        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            // User already authenticated
            userId = user.uid

            // Create user instance with userId, latitude, longitude as 0.0, and the image URL
            val createUser = CreateUser(
                name,
                email,
                password,
                code,
                "true",
                "na",
                "na",
                "na",
                userId,
                0.0,
                0.0,
                imageUrl
            )

            // Add user data in Firebase Realtime Database
            reference.child(userId).setValue(createUser)
                .addOnCompleteListener { dbTask: Task<Void> ->
                    progressDialog.dismiss()
                    if (dbTask.isSuccessful) {
                        Toast.makeText(applicationContext, "Please wait while we create your account...", Toast.LENGTH_SHORT).show()
                        val myIntent = Intent(this@InviteCodeActivity, NavigationActivity::class.java)
                        startActivity(myIntent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Could not update user information", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // If the user is not already authenticated, proceed with normal registration
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        userId = user?.uid ?: ""

                        // Create user instance with userId, latitude, longitude as 0.0, and the image URL
                        val createUser = CreateUser(
                            name,
                            email,
                            password,
                            code,
                            "true",
                            "na",
                            "na",
                            "na",
                            userId,
                            0.0,
                            0.0,
                            imageUrl
                        )

                        // Add user data to Firebase Realtime Database
                        reference.child(userId).setValue(createUser)
                            .addOnCompleteListener { dbTask: Task<Void> ->
                                progressDialog.dismiss()
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(applicationContext, "User Registered successfully", Toast.LENGTH_SHORT).show()
                                    val myIntent = Intent(this@InviteCodeActivity, NavigationActivity::class.java)
                                    startActivity(myIntent)
                                    finish()
                                } else {
                                    Toast.makeText(applicationContext, "Could not register user", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}

data class CreateUser(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val code: String = "",
    val isSharing: String = "true",
    val extra1: String = "na",
    val extra2: String = "na",
    val extra3: String = "na",
    val userId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = ""
)

