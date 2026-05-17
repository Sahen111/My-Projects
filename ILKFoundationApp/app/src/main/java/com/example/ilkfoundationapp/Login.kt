package com.example.ilkfoundationapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks

class Login : AppCompatActivity() {

    //variables
    lateinit var tvSignUp: TextView
    lateinit var btnSignIn: Button
    lateinit var edEmail: EditText
    lateinit var edPassword: EditText
    //firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //typecast the variables
        tvSignUp = findViewById(R.id.tvSignUp)
        btnSignIn = findViewById(R.id.btnLogin)
        edEmail = findViewById(R.id.edtEmail)
        edPassword = findViewById(R.id.edtPassword)

        //initialize firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        //btn click event
        btnSignIn.setOnClickListener {
            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()

            //error checks
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }//if ends
            loginUser(email, password)
        }

        tvSignUp.setOnClickListener {
            val intentReg = Intent(this@Login, Register::class.java)
            startActivity(intentReg)}

        //function call
        linkSetUp()
    }

    fun loginUser(email:String, password:String)
    {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(this, "You are now logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else
                {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //function for the sign up link
    private fun linkSetUp()
    {
        val signUpLink = Link("Sign Up")
            .setTextColor(Color.parseColor("#ffc838"))
            .setHighlightAlpha(.4f)
            .setUnderlined(false)
            .setBold(true)
            .setOnClickListener{
                val intentLog = Intent(this@Login,Register::class.java)
                startActivity(intentLog)
            }
        tvSignUp.applyLinks(signUpLink)
    }
}