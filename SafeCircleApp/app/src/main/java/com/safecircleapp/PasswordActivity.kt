package com.safecircleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class PasswordActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var edtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        edtPassword = findViewById(R.id.editTextTextPassword2)

        val myIntent = intent
        if (myIntent != null) {
            email = myIntent.getStringExtra("email") ?: ""
        }
    }
    fun goToNamePicActivity(v: View) {
        val password = edtPassword.text.toString()

        // Validate the password input
        if (password.isEmpty()) {
            edtPassword.error = "Password is required"
            edtPassword.requestFocus()
            return
        }

        if (password.length <= 6) {
            edtPassword.error = "Password length should be more than 6 characters"
            edtPassword.requestFocus()
            return
        }

        if (!password.matches(".*[A-Z].*".toRegex())) {
            edtPassword.error = "Password must contain at least one uppercase letter"
            edtPassword.requestFocus()
            return
        }

        if (!password.matches(".*\\d.*".toRegex())) {
            edtPassword.error = "Password must contain at least one number"
            edtPassword.requestFocus()
            return
        }

        // If all validations pass, proceed to the next activity
        val myIntent = Intent(this@PasswordActivity, NameActivity::class.java)
        myIntent.putExtra("email", email)
        myIntent.putExtra("password", password)
        startActivity(myIntent)
        finish()
    }
}