package com.safecircleapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtEmail = findViewById(R.id.editTextTextEmailAddress2)
        auth = FirebaseAuth.getInstance()
        dialog = ProgressDialog(this)
    }

    fun goToPasswordActivity(v: View) {
        val email = edtEmail.text.toString().trim()

        // Validate the email input
        if (email.isEmpty()) {
            edtEmail.error = "Email address is required"
            edtEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Please enter a valid email address"
            edtEmail.requestFocus()
            return
        }

        dialog.setMessage("Checking email address...")
        dialog.show()

        // Check if this email is already registered or not
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                dialog.dismiss()
                if (task.isSuccessful) {
                    val isEmailRegistered = !task.result?.signInMethods.isNullOrEmpty()

                    if (!isEmailRegistered) {
                        // if the email does not exist, create this email with the user
                        val myIntent = Intent(this@RegisterActivity, PasswordActivity::class.java)
                        myIntent.putExtra("email", email)
                        startActivity(myIntent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "This email is already registered", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "An error occurred: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}