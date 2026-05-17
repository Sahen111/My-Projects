package com.example.timewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.credentials.CreateEntry
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class MainMenu : AppCompatActivity() {

    lateinit var btnCreateEntry: Button
    lateinit var btnListEntries: Button
    lateinit var btnTotalNumHours: Button
    lateinit var btnMinMaxGoals: Button
    lateinit var btnAboutUs: Button
    lateinit var btnGraph: ImageButton
    lateinit var btnGoalStats: ImageButton
    lateinit var btnTimer: ImageButton

    private lateinit var txtMinDailyGoal: TextView
    private lateinit var txtMaxDailyGoal: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtLevel: TextView
    private lateinit var txtProgress: TextView
    private lateinit var txtHoursToComplete: TextView
    private lateinit var database: DatabaseReference

    data class DailyGoal(
        val date: String? = null,
        val minDailyGoal: Int? = null,
        val maxDailyGoal: Int? = null
    )

    data class Task(
        val category: String? = null,
        val endTimeString: String? = null,
        val startDateString: String? = null,
        val startTimeString: String? = null,
        val taskDesc: String? = null,
        val taskName: String? = null,
        val totalTimeString: String? = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        btnCreateEntry = findViewById(R.id.btnCreateEntry)
        btnListEntries = findViewById(R.id.btnListEntries)
        btnTotalNumHours = findViewById(R.id.btnTotalNumHours)
        btnMinMaxGoals = findViewById(R.id.btnMinMaxGoals)
        txtMinDailyGoal = findViewById(R.id.txtMinDailyGoal)
        txtMaxDailyGoal = findViewById(R.id.txtMaxDailyGoal)
        progressBar = findViewById(R.id.progressBar)
        txtLevel = findViewById(R.id.txtLevel)
        txtProgress = findViewById(R.id.txtProgress)
        txtHoursToComplete = findViewById(R.id.txtHoursToComplete)
        btnAboutUs = findViewById(R.id.btnAboutUs)
        btnGraph = findViewById(R.id.btnGraph)
        btnGoalStats = findViewById(R.id.btnGoalStats)
        btnTimer = findViewById(R.id.btnTimer)

        database = FirebaseDatabase.getInstance().reference







// Get today's date in yyyy-MM-dd format
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Query Firebase to get the daily goal closest to the current date
        val query = database.child("daily_goals").orderByChild("date").endAt(currentDate).limitToLast(1)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dailyGoalSnapshot in snapshot.children) {
                        val minGoal = dailyGoalSnapshot.child("min_daily_goal").getValue(Int::class.java)
                        val maxGoal = dailyGoalSnapshot.child("max_daily_goal").getValue(Int::class.java)

                        if (minGoal != null && maxGoal != null) {
                            txtMinDailyGoal.text = minGoal.toString()
                            txtMaxDailyGoal.text = maxGoal.toString()
                        } else {
                            txtMinDailyGoal.text = "N/A"
                            txtMaxDailyGoal.text = "N/A"
                        }
                    }
                } else {
                    txtMinDailyGoal.text = "Goal not set"
                    txtMaxDailyGoal.text = "Goal not set"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                txtMinDailyGoal.text = "Error loading goal"
                txtMaxDailyGoal.text = "Error loading goal"
            }
        })

        fetchAndCalculateTotalTime()

        btnCreateEntry.setOnClickListener {
            val intentReg = Intent(this@MainMenu, HomeScreen::class.java)
            startActivity(intentReg)
        }

        btnListEntries.setOnClickListener {
            val intentReg = Intent(this@MainMenu, ListOfEntries::class.java)
            startActivity(intentReg)
        }

        btnTotalNumHours.setOnClickListener {
            val intentReg = Intent(this@MainMenu, TotalHours::class.java)
            startActivity(intentReg)
        }

        btnMinMaxGoals.setOnClickListener {
            val intentReg = Intent(this@MainMenu, MinMaxDailyGoals::class.java)
            startActivity(intentReg)
        }

        btnAboutUs.setOnClickListener {
            val intentReg = Intent(this@MainMenu, AboutUs::class.java)
            startActivity(intentReg)
        }

        btnGraph.setOnClickListener {
            val intentReg = Intent(this@MainMenu, Graph::class.java)
            startActivity(intentReg)
        }

        btnGoalStats.setOnClickListener {
            val intentReg = Intent(this@MainMenu, GoalStats::class.java)
            startActivity(intentReg)
        }

        btnTimer.setOnClickListener {
            val intentReg = Intent(this@MainMenu, com.example.timewise.Timer::class.java)
            startActivity(intentReg)
        }
    }




    private fun fetchAndCalculateTotalTime() {
        database.child("items").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalMinutes = 0

                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    task?.totalTimeString?.let {
                        totalMinutes += convertTimeStringToMinutes(it)
                    }
                }

                val totalHours = totalMinutes / 60
                val level = totalHours / 20
                val progress = totalHours % 20

                txtLevel.text = "Level ${level + 1}"
                progressBar.progress = progress
                txtProgress.text = "20 Hours"
                txtHoursToComplete.text = "${20 - progress} Hours to Complete"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainMenu", "Error fetching tasks", error.toException())
            }
        })
    }

    private fun convertTimeStringToMinutes(timeString: String): Int {
        val parts = timeString.split(":")
        return if (parts.size == 2) {
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            hours * 60 + minutes
        } else {
            0
        }
    }


}