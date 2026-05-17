package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            // Start MainActivity after 3 seconds
            val intent = Intent(this, LoadingScreen::class.java)
            startActivity(intent)
            finish() // Finish the SplashScreen activity
        }, 3000)
    }
}