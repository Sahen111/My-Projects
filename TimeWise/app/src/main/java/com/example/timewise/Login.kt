package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var btnSignIn: Button
    lateinit var edEmail: EditText
    lateinit var edPassword: EditText
    lateinit var btnSignUp: Button
    //firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //typecasts
        btnSignIn = findViewById(R.id.btnSignIn)
        edEmail = findViewById(R.id.edEmail)
        edPassword = findViewById(R.id.edPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

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
        btnSignUp.setOnClickListener{
            val intentReg = Intent(this@Login,Register::class.java)
            startActivity(intentReg)
        }
    }//Method
    fun loginUser(email:String, password:String)
    {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful)
                {
                    Toast.makeText(this, "You are now logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainMenu::class.java)
                    startActivity(intent)
                } else
                {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}