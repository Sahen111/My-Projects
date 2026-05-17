package com.example.ilkfoundationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.util.Timer
import java.util.TimerTask

class Splash : AppCompatActivity() {

    //variables
    lateinit var ivSplash: ImageView
    //set the delay before the screen go to the next screen
    private val delay: Long = 5600

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //typecast the variables
        ivSplash = findViewById(R.id.ivSplash)

        //animated image code
        Glide.with(this).load(R.drawable.ilksplash).into(ivSplash)

        //create a timer object
        val runSplash = Timer()

        //Task to do after
        val showSplash = object : TimerTask()
        {
            override fun run()
            {
                //will close the main
                finish()
                //move to the next screen
                val intentOne = Intent(this@Splash,Register::class.java)
                startActivity(intentOne)
            }//run method ends
        }//object ends
        runSplash.schedule(showSplash,delay)
    }
}