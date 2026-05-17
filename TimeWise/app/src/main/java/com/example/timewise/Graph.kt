package com.example.timewise

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Graph : AppCompatActivity() {

    private lateinit var edOne: EditText
    private lateinit var edTwo: EditText
    private lateinit var btnPlot: Button
    private lateinit var lineChart: LineChart
    private lateinit var database: DatabaseReference
    private val goalEntries = mutableListOf<GoalModel>()
    lateinit var btnHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        edOne = findViewById(R.id.edOne)
        edTwo = findViewById(R.id.edTwo)
        btnPlot = findViewById(R.id.btnPlot)
        lineChart = findViewById(R.id.lineChart)
        btnHome = findViewById(R.id.btnHome)
        database = FirebaseDatabase.getInstance().reference

        edOne.setOnClickListener {
            showDatePickerDialog(edOne)
        }

        edTwo.setOnClickListener {
            showDatePickerDialog(edTwo)
        }

        btnPlot.setOnClickListener {
            fetchGoalsAndPlotGraph()
        }

        btnHome.setOnClickListener {
            val intentReg = Intent(this@Graph, MainMenu::class.java)
            startActivity(intentReg)
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            editText.setText(dateFormat.format(selectedDate.time))
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun fetchGoalsAndPlotGraph() {
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
                plotGraph()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Graph, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun plotGraph() {
        val startDateString = edOne.text.toString()
        val endDateString = edTwo.text.toString()

        if (startDateString.isEmpty() || endDateString.isEmpty()) {
            Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val startDate = dateFormat.parse(startDateString)
            val endDate = dateFormat.parse(endDateString)

            val query = database.child("items")

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dateToHoursMap = mutableMapOf<String, Float>()

                    for (entrySnapshot in snapshot.children) {
                        val entryData = entrySnapshot.getValue(TaskModel::class.java)
                        if (entryData != null) {
                            val entryStartDate = dateFormat.parse(entryData.startDateString)
                            if (entryStartDate != null && entryStartDate >= startDate && entryStartDate <= endDate) {
                                val totalHours = parseTotalTimeString(entryData.totalTimeString)
                                val dateString = dateFormat.format(entryStartDate)
                                dateToHoursMap[dateString] = dateToHoursMap.getOrDefault(dateString, 0f) + totalHours
                            }
                        }
                    }

                    val dates = generateDateRange(startDateString, endDateString)
                    val entries = dates.mapIndexed { index, dateString ->
                        val totalHours = dateToHoursMap[dateString] ?: 0f
                        Entry(index.toFloat(), totalHours, dateString)
                    }

                    val dataSet = LineDataSet(entries, "Hours Worked")
                    dataSet.color = ColorTemplate.JOYFUL_COLORS[0]
                    dataSet.valueTextColor = ColorTemplate.JOYFUL_COLORS[0]

                    val minGoalEntries = mutableListOf<Entry>()
                    val maxGoalEntries = mutableListOf<Entry>()

                    for (date in dates) {
                        val goal = getGoalForDate(date)
                        goal?.let {
                            val minGoalEntry = Entry(dates.indexOf(date).toFloat(), it.min_daily_goal.toFloat())
                            minGoalEntries.add(minGoalEntry)

                            val maxGoalEntry = Entry(dates.indexOf(date).toFloat(), it.max_daily_goal.toFloat())
                            maxGoalEntries.add(maxGoalEntry)
                        }
                    }

                    val minGoalDataSet = LineDataSet(minGoalEntries, "Min Goal")
                    minGoalDataSet.color = Color.GREEN
                    minGoalDataSet.setDrawCircles(false)
                    minGoalDataSet.lineWidth = 2f
                    minGoalDataSet.mode = LineDataSet.Mode.STEPPED
                    minGoalDataSet.axisDependency = YAxis.AxisDependency.LEFT

                    val maxGoalDataSet = LineDataSet(maxGoalEntries, "Max Goal")
                    maxGoalDataSet.color = Color.RED
                    maxGoalDataSet.setDrawCircles(false)
                    maxGoalDataSet.lineWidth = 2f
                    maxGoalDataSet.mode = LineDataSet.Mode.STEPPED
                    maxGoalDataSet.axisDependency = YAxis.AxisDependency.LEFT

                    val lineData = LineData(dataSet, minGoalDataSet, maxGoalDataSet)
                    lineChart.data = lineData

                    val xAxis = lineChart.xAxis
                    xAxis.valueFormatter = IndexAxisValueFormatter(dates)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.labelRotationAngle = -45f
                    xAxis.setAvoidFirstLastClipping(true)
                    xAxis.granularity = 1f

                    lineChart.axisLeft.axisMinimum = 0f
                    lineChart.axisLeft.axisMaximum = 24f
                    lineChart.axisLeft.granularity = 1f

                    lineChart.description.isEnabled = false
                    lineChart.invalidate()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Graph, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error parsing dates", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parseTotalTimeString(totalTimeString: String?): Float {
        return totalTimeString?.split(":")?.let {
            val hours = it[0].toIntOrNull() ?: 0
            val minutes = it[1].toIntOrNull() ?: 0
            hours + minutes / 60f
        } ?: 0f
    }

    private fun generateDateRange(startDateString: String, endDateString: String): List<String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = dateFormat.parse(startDateString)
        val endDate = dateFormat.parse(endDateString)

        val dates = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        while (calendar.time.before(endDate) || calendar.time == endDate) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return dates
    }

    private fun getGoalForDate(date: String): GoalModel? {
        return goalEntries.lastOrNull { it.date <= date }
    }

    data class TaskModel(
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
}
