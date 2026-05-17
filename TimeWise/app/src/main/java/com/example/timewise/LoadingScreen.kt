package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar

class LoadingScreen : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        progressBar = findViewById(R.id.progressBar)

        progressBar.progress = 0

        val progressIncrement = 100 / 50

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (progressBar.progress < 100) {
                    progressBar.progress += progressIncrement
                    handler.postDelayed(this, 100) // Repeat every 100 milliseconds
                } else {
                    // Progress reaches 100%, start HomeScreen activity
                    val intent = Intent(this@LoadingScreen, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 100)
    }
}