package com.example.ilkfoundationapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks

class Register : AppCompatActivity() {

    //variables
    lateinit var edName: EditText
    lateinit var edEmail: EditText
    lateinit var edPassword: EditText
    lateinit var edCnfmPassword: EditText
    lateinit var btnSignUp: Button
    lateinit var tvLogin: TextView

    //firebase
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //typecast the variables
        edName = findViewById(R.id.edName)
        edEmail = findViewById(R.id.edEmail)
        edPassword = findViewById(R.id.edPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        edCnfmPassword = findViewById(R.id.edCnfmPassword)
        tvLogin = findViewById(R.id.tvLogin)

        //firebase
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        //btn click events
        btnSignUp.setOnClickListener {
            registerUser()
        }

        //function call
        linkSetUp()

    }//end of onCreate

    //function to register user
    private fun registerUser()
    {
        try {
            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()
            val confirmPassword = edCnfmPassword.text.toString().trim()

            //validation checks
            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return
            }
            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Password can't be blank", Toast.LENGTH_SHORT).show()
                return
            }
            if(TextUtils.isEmpty(confirmPassword))
            {
                Toast.makeText(this, "Enter a matching password", Toast.LENGTH_SHORT).show()
                return
            }

            //attempt registration --> firebase
            //this is a method
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(this, "Reg complete, you may now login", Toast.LENGTH_SHORT).show()
                        //move to the home screen
//                        val intentLog = Intent(this@Register,MainActivity::class.java)
//                        startActivity(intentLog)

                    }else
                    {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }//listener ends
        }

        catch (e: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }

    }


    //function for the login link
    private fun linkSetUp()
    {
        val signUpLink = Link("Login")
            .setTextColor(Color.parseColor("#ffc838"))
            .setHighlightAlpha(.4f)
            .setUnderlined(false)
            .setBold(true)
            .setOnClickListener{
                val intentLog = Intent(this@Register,Login::class.java)
                startActivity(intentLog)
            }
        tvLogin.applyLinks(signUpLink)
    }
}