package com.example.timewise

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.graphics.ColorUtils
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class GoalStats : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var progressBarContainer: LinearLayout
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val goalEntries = mutableListOf<GoalModel>()

    lateinit var btnHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_stats)

        btnHome = findViewById(R.id.btnHome)

        btnHome.setOnClickListener {
            val intentReg = Intent(this@GoalStats, MainMenu::class.java)
            startActivity(intentReg)
        }

        progressBarContainer = findViewById(R.id.progressBarContainer)
        database = FirebaseDatabase.getInstance().reference

        findViewById<Button>(R.id.maxHoursButton).setOnClickListener { glowBars("maxBar", Color.RED) }
        findViewById<Button>(R.id.totalTimeButton).setOnClickListener { glowBars("progressBar", Color.BLUE) }
        findViewById<Button>(R.id.minHoursButton).setOnClickListener { glowBars("minBar", Color.GREEN) }

        fetchGoalsAndProgressData()
    }

    private fun fetchGoalsAndProgressData() {
        val query = database.child("daily_goals").orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                goalEntries.clear()
                for (goalSnapshot in snapshot.children) {
                    val goalData = goalSnapshot.getValue(GoalModel::class.java)
                    if (goalData != null) {
                        goalEntries.add(goalData)
                    }
                }
                fetchProgressData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchProgressData() {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val startDate = calendar.time

        val progressDataList = mutableListOf<ProgressData>()

        val query = database.child("items")
            .orderByChild("startDateString")
            .startAt(dateFormat.format(startDate))
            .endAt(dateFormat.format(endDate))

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val task = data.getValue(TimesheetModel::class.java)
                    if (task != null) {
                        val progress = parseTotalTimeString(task.totalTimeString)
                        val goal = getGoalForDate(task.startDateString)
                        progressDataList.add(ProgressData(task.startDateString, progress, goal.min_daily_goal.toFloat(), goal.max_daily_goal.toFloat()))
                    }
                }
                displayProgressBars(progressDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun parseTotalTimeString(totalTimeString: String?): Float {
        return totalTimeString?.split(":")?.let {
            val hours = it[0].toIntOrNull() ?: 0
            val minutes = it[1].toIntOrNull() ?: 0
            hours + minutes.toFloat() / 60
        } ?: 0f
    }

    private fun getGoalForDate(date: String): GoalModel {
        return goalEntries.lastOrNull { it.date <= date } ?: GoalModel()
    }

    private fun displayProgressBars(progressDataList: List<ProgressData>) {
        for (progressData in progressDataList) {
            val container = LinearLayout(this)
            container.orientation = LinearLayout.VERTICAL
            container.setPadding(16, 16, 16, 16)

            val maxProgressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(10)
                ).apply {
                    bottomMargin = dpToPx(8)
                }
                max = 24
                progressDrawable = createProgressDrawable(Color.RED)
                progress = (progressData.maxGoal * 24 / 24).toInt()
                tag = "maxBar"
            }

            val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(10)
                ).apply {
                    bottomMargin = dpToPx(8)
                }
                max = 24
                progressDrawable = createProgressDrawable(Color.BLUE)
                progress = (progressData.progress * 24 / 24).toInt()
                tag = "progressBar"
            }

            val minProgressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(10)
                ).apply {
                    bottomMargin = dpToPx(8)
                }
                max = 24
                progressDrawable = createProgressDrawable(Color.GREEN)
                progress = (progressData.minGoal * 24 / 24).toInt()
                tag = "minBar"
            }

            val dateRange = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = displayDateFormat.format(dateFormat.parse(progressData.date))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                setTextColor(Color.BLACK)
                setTypeface(null, Typeface.BOLD)
            }

            container.addView(dateRange)
            container.addView(maxProgressBar)
            container.addView(progressBar)
            container.addView(minProgressBar)

            progressBarContainer.addView(container)

            val divider = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(1)
                )
                setBackgroundColor(Color.LTGRAY)
            }
            progressBarContainer.addView(divider)
        }
    }



    private fun glowBars(targetTag: String, color: Int) {
        for (i in 0 until progressBarContainer.childCount) {
            val child = progressBarContainer.getChildAt(i)
            if (child is LinearLayout) {
                for (j in 0 until child.childCount) {
                    val view = child.getChildAt(j)
                    if (view is ProgressBar && view.tag == targetTag) {
                        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f)
                        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f)
                        scaleX.repeatCount = ObjectAnimator.INFINITE
                        scaleX.repeatMode = ObjectAnimator.REVERSE
                        scaleY.repeatCount = ObjectAnimator.INFINITE
                        scaleY.repeatMode = ObjectAnimator.REVERSE

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(scaleX, scaleY)
                        animatorSet.duration = 600
                        animatorSet.start()

                        view.progressDrawable = createProgressDrawable(color)

                        Handler(Looper.getMainLooper()).postDelayed({
                            animatorSet.cancel()
                            view.scaleX = 1f
                            view.scaleY = 1f
                        }, 3000)
                    }
                }
            }
        }
    }





    private fun createProgressDrawable(color: Int): LayerDrawable {
        val background = ColorDrawable(Color.LTGRAY)
        val progress = ClipDrawable(ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL)

        return LayerDrawable(arrayOf(background, progress)).apply {
            setId(0, android.R.id.background)
            setId(1, android.R.id.progress)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}

data class ProgressData(val date: String, val progress: Float, val minGoal: Float, val maxGoal: Float)

data class TimesheetModel(
    val taskName: String = "",
    val taskDesc: String = "",
    val category: String = "",
    val startDateString: String = "",
    val startTimeString: String = "",
    val endTimeString: String = "",
    val totalTimeString: String = ""
)

data class GoalModel(
    val date: String = "",
    val max_daily_goal: Int = 0,
    val min_daily_goal: Int = 0
)


