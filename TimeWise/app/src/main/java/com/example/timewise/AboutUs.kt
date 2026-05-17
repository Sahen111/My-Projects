package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class AboutUs : AppCompatActivity() {

    lateinit var btnHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        btnHome = findViewById(R.id.btnHome)

        btnHome.setOnClickListener {
            val intentReg = Intent(this@AboutUs, MainMenu::class.java)
            startActivity(intentReg)
        }
    }
}