package com.safecircleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var signInButton: SignInButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Set the content view
        setContentView(R.layout.activity_main)

        signInButton = findViewById(R.id.btnGoogleSignIn)

        // Check if user is logged in
        if (auth.currentUser != null) {
            // If the user is already logged in, proceed to the Navigation activity
            navigateToNavigationActivity()
        } else {
            // if the user is not logged in, show login/sign up options
            findViewById<View>(R.id.btnSignIn).setOnClickListener {
                val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(loginIntent)
            }

            findViewById<View>(R.id.btnSignUp).setOnClickListener {
                val registerIntent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(registerIntent)
            }

            // Set the click listener for the Google Sign-In button
            signInButton.setOnClickListener {
                signInWithGoogle()
            }
        }
    }



    private fun signInWithGoogle() {
        // Create the Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("976017310000-dfo7f7kv4m7o3m3bi9ni79ntv67k324f.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account.email // Get the user's email

            // Create a credential with the Google account token
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            // Authenticate with Firebase using the Google account credential
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Check if the user has an existing account
                        checkIfUserExistsInFirebase(email)
                    } else {
                        // Authentication failed message
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            Toast.makeText(this, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfUserExistsInFirebase(email: String?) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users")

        email?.let {
            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // if the user already exists, navigate to NavigationActivity
                        navigateToNavigationActivity()
                    } else {
                        // if the user does not exist, navigate to NameActivity
                        sendToNameActivity(email)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error checking user: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun sendToNameActivity(email: String?) {
        val myIntent = Intent(this@MainActivity, NameActivity::class.java)
        myIntent.putExtra("email", email)
        myIntent.putExtra("password", "GooglePassword")
        startActivity(myIntent)
        finish()
    }

    private fun navigateToNavigationActivity() {
        val myIntent = Intent(this@MainActivity, NavigationActivity::class.java)
        startActivity(myIntent)
        finish()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}