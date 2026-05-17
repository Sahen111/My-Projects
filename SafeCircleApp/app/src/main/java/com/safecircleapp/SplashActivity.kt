package com.safecircleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // Start MainActivity after 2 seconds
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
            finish() // Finish the SplashScreen activity
        }, 2000)
    }
}