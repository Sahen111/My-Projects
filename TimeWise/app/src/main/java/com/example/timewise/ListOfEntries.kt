package com.example.timewise

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.DropBoxManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ListOfEntries : AppCompatActivity() {

    private lateinit var edtTextStartDate: EditText
    private lateinit var edtTextEndDate: EditText
    private lateinit var btnViewEntries: Button
    private lateinit var database: DatabaseReference
    private lateinit var btnHome: ImageButton
    private lateinit var txtListOfEntries: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_entries)

        edtTextStartDate = findViewById(R.id.edtTextStartDate)
        edtTextEndDate = findViewById(R.id.edtTextEndDate)
        btnViewEntries = findViewById(R.id.btnViewEntries)
        txtListOfEntries = findViewById(R.id.txtListOfEntries)
        btnHome = findViewById(R.id.btnHome)

        btnHome.setOnClickListener {
            val intentReg = Intent(this@ListOfEntries, MainMenu::class.java)
            startActivity(intentReg)
        }
        database = FirebaseDatabase.getInstance().reference

        edtTextStartDate.isFocusable = false
        edtTextStartDate.isClickable = true
        edtTextEndDate.isFocusable = false
        edtTextEndDate.isClickable = true


        edtTextStartDate.setOnClickListener {
            showDatePicker(edtTextStartDate)
        }

        edtTextEndDate.setOnClickListener {
            showDatePicker(edtTextEndDate)
        }

        btnViewEntries.setOnClickListener {
            viewEntries()
        }
    }

    fun onViewEntriesClicked(view: View) {
        // Load entries from Firebase when the button is clicked
        loadEntries()
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



    private fun viewEntries() {
        val startDateString = edtTextStartDate.text.toString()
        val endDateString = edtTextEndDate.text.toString()

        if (startDateString.isEmpty() || endDateString.isEmpty()) {
            Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val startDate = dateFormat.parse(startDateString)
            val endDate = dateFormat.parse(endDateString)

            val query = database.child("items")
                .orderByChild("startDateString")
                .startAt(startDateString)
                .endAt(endDateString)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val entries = mutableListOf<TaskModel>()
                    for (data in snapshot.children) {
                        val entry = data.getValue(TaskModel::class.java)
                        entry?.let {
                            entries.add(it)
                        }
                    }
                    displayEntries(entries)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ListOfEntries, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: ParseException) {
            Toast.makeText(this, "Error parsing dates", Toast.LENGTH_SHORT).show()
        }
    }


    private fun displayEntries(entries: List<TaskModel>) {
        val formattedEntries = buildString {
            entries.forEach { entry ->
                val startDate = entry.startDateString ?: return@forEach
                val startTime = entry.startTimeString ?: return@forEach
                val endTime = entry.endTimeString ?: return@forEach
                val totalTime = entry.totalTimeString ?: return@forEach

                append("Date: $startDate\n")
                append("Start Time: $startTime\n")
                append("End Time: $endTime\n")
                append("Total Time: $totalTime\n")
                append("Name: ${entry.taskName}\n")
                append("Description: ${entry.taskDesc}\n\n")
            }
        }
        txtListOfEntries.text = formattedEntries

        val spannableString = SpannableString(txtListOfEntries.text)
        entries.forEach { entry ->
            val startDate = entry.startDateString ?: return@forEach
            val startIndex = formattedEntries.indexOf("Date: $startDate")
            val endIndex = startIndex + startDate.length + 6 // 6 is the length of "Date: "
            spannableString.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        txtListOfEntries.setText(spannableString, TextView.BufferType.SPANNABLE)
    }




    private fun loadEntries() {
    val entriesRef = database.child("items")
    entriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val entriesList = mutableListOf<String>()

            for (entrySnapshot in snapshot.children) {
                val entryData = entrySnapshot.getValue(TaskModel::class.java)

                val entryString = entryData?.let { formatEntry(it) }
                entryString?.let { entriesList.add(it) }
            }

            txtListOfEntries.text = entriesList.joinToString("\n")
        }

        override fun onCancelled(error: DatabaseError) {
        }
    })
}

    private fun formatEntry(entryData: TaskModel): String {
        return "Name: ${entryData.taskName}, Description: ${entryData.taskDesc}, Start Date: ${entryData.startDateString}"
    }

}