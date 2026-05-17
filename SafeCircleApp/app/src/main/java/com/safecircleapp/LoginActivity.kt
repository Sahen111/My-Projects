package com.safecircleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.editTextTextEmailAddress)
        edtPassword = findViewById(R.id.editTextTextPassword)
        auth = FirebaseAuth.getInstance()
    }

    fun login(v: View) {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        // Check if email and password are entered
        if (email.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your email", Toast.LENGTH_LONG).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(applicationContext, "Please enter your password", Toast.LENGTH_LONG).show()
            return
        }

        // Check for valid email format
        if (!isValidEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            return
        }

        // Check for password requirements
        if (!isValidPassword(password)) {
            Toast.makeText(applicationContext, "Password must be at least 6 characters, contain a capital letter, and a number", Toast.LENGTH_LONG).show()
            return
        }

        // Proceed with Firebase authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "User logged in successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginActivity, NavigationActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Wrong email or password", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to validate password strength
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isDigit() }
    }


}