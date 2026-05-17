package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    //variables
    private lateinit var edUsername: EditText
    private lateinit var edRegEmail: EditText
    private lateinit var edRegPassword: EditText
    private lateinit var edRegConfirmPassword: EditText
    private lateinit var btnRegister: Button
    //firebase
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //typecast the variables
        edUsername = findViewById(R.id.edUsername)
        edRegEmail = findViewById(R.id.edRegEmail)
        edRegPassword = findViewById(R.id.edRegPassword)
        edRegConfirmPassword = findViewById(R.id.edRegConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        //firebase
        mAuth = FirebaseAuth.getInstance()

        //Code for the register button
        btnRegister.setOnClickListener()
        {
            registerUser()
        }

    }//on create ends
    //method to register
    private fun registerUser()
    {
        try {
            val email = edRegEmail.text.toString().trim()
            val password = edRegPassword.text.toString().trim()
            val confirmpassword = edRegConfirmPassword.text.toString().trim()


            //validation checks
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "please enter a valid email", Toast.LENGTH_SHORT).show()
                return
            }
            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Password can't be blank", Toast.LENGTH_SHORT).show()
                return
            }
            if (TextUtils.isEmpty(confirmpassword))
            {
                Toast.makeText(this, "Enter a matching password", Toast.LENGTH_SHORT).show()
            }

            //attempt registration --> Firebase
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(this, "Reg complete, you may now login", Toast.LENGTH_SHORT).show()
                    }else
                    {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}