package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MinMaxDailyGoals : AppCompatActivity() {

    private lateinit var edtTextMinDailyGoal: EditText
    private lateinit var edtTextMaxDailyGoal: EditText
    private lateinit var database: DatabaseReference
    private lateinit var btnHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_min_max_daily_goals)

        edtTextMinDailyGoal = findViewById(R.id.txtMinDailyGoal)
        edtTextMaxDailyGoal = findViewById(R.id.txtMaxDailyGoal)
        btnHome = findViewById(R.id.btnHome)

        btnHome.setOnClickListener {
            val intentReg = Intent(this@MinMaxDailyGoals, MainMenu::class.java)
            startActivity(intentReg)
        }

        database = FirebaseDatabase.getInstance().reference

        findViewById<Button>(R.id.btnSaveGoals).setOnClickListener {
            saveDailyGoals()
        }
    }

    private fun saveDailyGoals() {
        val minDailyGoal = edtTextMinDailyGoal.text.toString().toDoubleOrNull() ?: 0.0
        val maxDailyGoal = edtTextMaxDailyGoal.text.toString().toDoubleOrNull() ?: 0.0

        // Get today's date in yyyy-MM-dd format
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Generate a new unique key for each entry
        val entryKey = database.child("daily_goals").push().key ?: ""

        val goalsData = HashMap<String, Any>()
        goalsData["min_daily_goal"] = minDailyGoal
        goalsData["max_daily_goal"] = maxDailyGoal
        goalsData["date"] = currentDate

        // Save the new entry to Firebase
        database.child("daily_goals").child(entryKey).setValue(goalsData)
            .addOnSuccessListener {
                Toast.makeText(this@MinMaxDailyGoals, "Goals saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this@MinMaxDailyGoals, "Failed to save goals: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}