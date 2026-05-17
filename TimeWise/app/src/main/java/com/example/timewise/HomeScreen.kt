package com.example.timewise

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class HomeScreen : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_CAMERA = 1001
    }


    private lateinit var cardView: CardView

    //variables
    lateinit var spinner: Spinner
    lateinit var edname: EditText
    lateinit var edDescription: EditText
    lateinit var btnStartDate: Button
    lateinit var btnStartTime: Button
    lateinit var btnEndTime: Button
    lateinit var btnSave: Button
    lateinit var btnAdvCamera: Button
    lateinit var txtStartDate : TextView
    lateinit var txtEndDate : TextView
    lateinit var txtStartTime : TextView
    lateinit var txtEndTime : TextView
    lateinit var edtNewCategory: EditText
    lateinit var btnNewCategory: Button
    lateinit var btnHome: ImageButton
    //globals
    var startDate: Date? = null
    var startTime: Date? = null
    var endTime: Date? = null
    private var imageURL: String? = null
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)


        cardView = findViewById(R.id.cardView)
        spinner = findViewById(R.id.spnCategory)
        edname = findViewById(R.id.edtName)
        edDescription = findViewById(R.id.edtDescription)
        btnStartTime = findViewById(R.id.btnStartTime)
        btnStartDate = findViewById(R.id.btnStartDate)
        btnEndTime = findViewById(R.id.btnEndTime)
        btnSave = findViewById(R.id.btnSave)
        btnAdvCamera = findViewById(R.id.btnAddPic)
        txtStartDate = findViewById(R.id.txtStartDate)
        txtStartTime = findViewById(R.id.txtStartTime)
        txtEndTime = findViewById(R.id.txtEndTime)
        btnNewCategory = findViewById(R.id.btnNewCategory)
        btnHome = findViewById(R.id.btnHome)

        btnHome.setOnClickListener {
            val intentReg = Intent(this@HomeScreen, MainMenu::class.java)
            startActivity(intentReg)
        }

        database = FirebaseDatabase.getInstance().reference
        val spinnerAdapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        btnStartDate.setOnClickListener{showDatePicker(startDateListener)}
        btnStartTime.setOnClickListener{showTimePicker(startTimeListener)}
        btnEndTime.setOnClickListener{showTimePicker(endTimeListener)}

        btnSave.setOnClickListener()
        {
            val selectedItem = spinner.selectedItem as String
            val taskName = edname.text.toString()
            val taskDesc = edDescription.text.toString()
            if (taskName.isEmpty()) {
                edname.error = "Please enter task name"
                return@setOnClickListener
            }
            if (taskDesc.isEmpty())
            {
                edDescription.error = "Please enter task description"
                return@setOnClickListener
            }
            saveToFirebase(selectedItem, taskName, taskDesc, imageURL)
        }

        btnAdvCamera.setOnClickListener{
            val intentTwo = Intent(this, com.example.timewise.Camera::class.java)
            startActivity(intentTwo)
        }

        btnNewCategory.setOnClickListener {
            showNewCategoryDialog()
        }
        loadCategoriesFromFirebase()
    }






    private fun showNewCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter New Category")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val newCategory = input.text.toString().trim()
            if (newCategory.isNotEmpty()) {
                addNewCategoryToFirebase(newCategory)
            } else {
                Toast.makeText(this, "Please enter a category", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addNewCategoryToFirebase(newCategory: String) {
        val categoryRef = database.child("categories").push()
        categoryRef.setValue(newCategory)
            .addOnSuccessListener {
                Toast.makeText(this, "New category added: $newCategory", Toast.LENGTH_SHORT).show()
                loadCategoriesFromFirebase()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Failed to add category: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadCategoriesFromFirebase() {
        val categoryList = mutableListOf<String>()

        database.child("categories").get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach { categorySnapshot ->
                val category = categorySnapshot.getValue(String::class.java)
                category?.let {
                    categoryList.add(it)
                }
            }

            // Update spinner adapter with categories from Firebase
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryList)
            spinner.adapter = adapter
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }



    fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener)
    {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,
            dateSetListener,year,month,day)
        datePickerDialog.show()
    }

    fun showTimePicker (timeSetListener: TimePickerDialog.OnTimeSetListener)
    {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, timeSetListener, hour, minute, true)
        timePickerDialog.show()
    }

    val startDateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month:Int, day:Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, day)
        startDate = selectedCalendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val selectedDateString = dateFormat.format(startDate!!)
        txtStartDate.text = selectedDateString
    }

    private val startTimeListener =
        TimePickerDialog.OnTimeSetListener{ _: TimePicker, hourOfDay:Int, minute:Int ->

            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.time = startDate!!
            selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedCalendar.set(Calendar.MINUTE, minute)
            startTime = selectedCalendar.time
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTimeString = timeFormat.format(startTime)
            txtStartTime.text = selectedTimeString
        }





    private val endTimeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, minute: Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.time = startDate ?: Date()
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        val selectedEndTime = selectedCalendar.time

        if (startTime != null && selectedEndTime.before(startTime)) {
            Toast.makeText(this, "End time cannot be before start time", Toast.LENGTH_SHORT).show()
        } else {
            endTime = selectedEndTime
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTimeString = timeFormat.format(endTime!!)
            txtEndTime.text = selectedTimeString
        }
    }



    private fun saveToFirebase(selectedItem: String, taskName: String, taskDesc: String, imageURL: String?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Take the info from the TextFields
        val startDateString = txtStartDate.text.toString()
        val startTimeString = txtStartTime.text.toString()
        val endTimeString = txtEndTime.text.toString()

        // Now parse for format to firebase
        val startDate = dateFormat.parse(startDateString)
        val startTime = timeFormat.parse(startTimeString)
        val endTime = timeFormat.parse(endTimeString)

        // Calculate total time
        val totalTimeMillis = endTime.time - startTime.time
        val totalMinutes = totalTimeMillis / (1000 * 60)
        val totalHours = totalMinutes / 60
        val minutesRemaining = totalMinutes % 60
        val totalTimeString = String.format(Locale.getDefault(), "%02d:%02d", totalHours, minutesRemaining)

        val key = database.child("items").push().key
        if (key != null) {
            val task = TaskModel(
                taskName,
                taskDesc,
                selectedItem,
                startDateString,
                startTimeString,
                endTimeString,
                totalTimeString,
                imageURL
            )

            // Save the task to Firebase
            database.child("items").child(key).setValue(task)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data added to Firebase", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageURL = data?.getStringExtra("imageURL").toString()
            val selectedItem = spinner.selectedItem as String
            val taskName = edname.text.toString()
            val taskDesc = edDescription.text.toString()
            if (taskName.isEmpty()) {
                edname.error = "Please enter task name"
                return
            }
            if (taskDesc.isEmpty()) {
                edDescription.error = "Please enter task description"
                return
            }
            saveToFirebase(selectedItem, taskName, taskDesc, imageURL)
        }
    }




}

//data class
data class TaskModel (
    var taskName: String? = null,
    var taskDesc: String? = null,
    var category: String? = null,
    var startDateString: String? = null,
    var startTimeString: String? = null,
    var endTimeString: String? = null,
    var totalTimeString: String? = null,
    var imageURL: String? = null
)