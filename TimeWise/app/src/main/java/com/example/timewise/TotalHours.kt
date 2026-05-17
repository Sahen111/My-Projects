package com.example.timewise

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TotalHours : AppCompatActivity() {

    private lateinit var edtTextStartDate: EditText
    private lateinit var edtTextEndDate: EditText
    private lateinit var spinnerCategories: Spinner
    private lateinit var btnViewTotalHours: Button
    private lateinit var database: DatabaseReference
    private lateinit var btnHome: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_hours)

        edtTextStartDate = findViewById(R.id.txtStartDate)
        edtTextEndDate = findViewById(R.id.txtEndDate)
        spinnerCategories = findViewById(R.id.spnCategories)
        btnViewTotalHours = findViewById(R.id.btnViewTotalHours)
        btnHome = findViewById(R.id.btnHome)
        database = FirebaseDatabase.getInstance().reference

        loadCategoriesFromFirebase()

        // Set up onClickListeners for the EditTexts to show date pickers
        edtTextStartDate.setOnClickListener { showDatePicker(edtTextStartDate) }
        edtTextEndDate.setOnClickListener { showDatePicker(edtTextEndDate) }

        // Set up onClickListener for the button to view total hours
        btnViewTotalHours.setOnClickListener { viewTotalHours() }

        btnHome.setOnClickListener {
            val intentReg = Intent(this@TotalHours, MainMenu::class.java)
            startActivity(intentReg)
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
            spinnerCategories.adapter = adapter
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Failed to load categories: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
                editText.setText(formattedDate)
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun viewTotalHours() {
        val startDateString = edtTextStartDate.text.toString()
        val endDateString = edtTextEndDate.text.toString()

        // Check if any of the EditText fields are empty
        if (startDateString.isEmpty() || endDateString.isEmpty()) {
            Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCategory = spinnerCategories.selectedItem.toString()

        // Parse start and end dates
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val startDate = dateFormat.parse(startDateString)
            val endDate = dateFormat.parse(endDateString)

            // Query Firebase to get entries within the selected period and category
            val query = database.child("items")
                .orderByChild("category")
                .equalTo(selectedCategory)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var totalHours = 0
                    var totalMinutes = 0

                    // Iterate over the entries and calculate total hours and minutes
                    for (entrySnapshot in snapshot.children) {
                        val entryData = entrySnapshot.getValue(TaskModel::class.java)

                        // Parse entry dates
                        val entryStartDate = dateFormat.parse(entryData?.startDateString)
                        val entryEndDate = dateFormat.parse(entryData?.startDateString)

                        if (entryStartDate != null && entryEndDate != null &&
                            entryStartDate >= startDate && entryEndDate <= endDate) {
                            // Add the total time for this entry to the total hours and minutes
                            val totalTimeString = entryData?.totalTimeString
                            val totalTime = parseTotalTimeString(totalTimeString)
                            totalHours += totalTime.first
                            totalMinutes += totalTime.second
                        }
                    }

                    // Adjust total hours if total minutes exceed 60
                    totalHours += totalMinutes / 60
                    totalMinutes %= 60

                    // Display total hours and minutes in an AlertDialog
                    val alertDialogBuilder = AlertDialog.Builder(this@TotalHours)
                    alertDialogBuilder.setTitle("Total Hours for $selectedCategory")
                    alertDialogBuilder.setMessage("Total Hours: $totalHours:$totalMinutes")
                    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    alertDialogBuilder.show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TotalHours, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: ParseException) {
            Toast.makeText(this, "Error parsing dates", Toast.LENGTH_SHORT).show()
        }
    }


    // Function to parse total time string and return hours and minutes
    private fun parseTotalTimeString(totalTimeString: String?): Pair<Int, Int> {
        val parts = totalTimeString?.split(":")
        if (parts?.size == 2) {
            val hours = parts[0].toIntOrNull() ?: 0
            val minutes = parts[1].toIntOrNull() ?: 0
            return Pair(hours, minutes)
        }
        return Pair(0, 0)
    }



}