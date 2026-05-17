package com.safecircleapp

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Switch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnAddRemoveEmergencyContacts: Button
    private lateinit var switchShareLocation: Switch
    private lateinit var userReference: DatabaseReference
    private lateinit var btnChangeAccountDetails: Button
    private lateinit var imgbtnBack: ImageButton
    private lateinit var spinnerLanguage: Spinner
    private lateinit var btnSaveLanguage: Button
    private var selectedLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnAddRemoveEmergencyContacts = findViewById(R.id.btnAddRemoveEmergencyContacts)
        btnChangeAccountDetails = findViewById(R.id.btnChangeAccountDetails)
        switchShareLocation = findViewById(R.id.switchShareLocation)
        imgbtnBack = findViewById(R.id.imgbtnBack)
        spinnerLanguage = findViewById(R.id.spinnerLanguage)
        btnSaveLanguage = findViewById(R.id.btnSaveLanguage)

        spinnerLanguage = findViewById(R.id.spinnerLanguage)

        // Initialize the spinner with language options
        val languages = arrayOf("English", "Afrikaans", "Tamil", "Zulu")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        // Initialize Firebase Database reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser?.uid ?: "")

        // Set an OnClickListener on the button
        btnChangeAccountDetails.setOnClickListener {
            // Create an Intent to start the EmergencyContactsActivity
            val intent = Intent(this, ChangeDetailsActivity::class.java)
            startActivity(intent)
        }

        // Set an OnClickListener on the button
        btnAddRemoveEmergencyContacts.setOnClickListener {
            // Create an Intent to start the EmergencyContactsActivity
            val intent = Intent(this, EmergencyContactsActivity::class.java)
            startActivity(intent)
        }
        // Load the current sharing status and set the switch accordingly
        loadSharingStatus()

        // Set listener for the spinner to get the selected language
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLanguage = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Set an OnClickListener on the save button
        btnSaveLanguage.setOnClickListener {
            selectedLanguage?.let { language ->
                changeAppLanguage(language)
            }
        }

        // Set a listener on the switch
        switchShareLocation.setOnCheckedChangeListener { _, isChecked ->
            val newSharingStatus = if (isChecked) "false" else "true"
            updateSharingStatus(newSharingStatus)
        }

        imgbtnBack.setOnClickListener {
            // Navigate back to NavigationActivity
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun loadSharingStatus() {
        userReference.child("sharing").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val sharingStatus = snapshot.getValue(String::class.java)
                // Update the switch based on sharing status
                switchShareLocation.isChecked = sharingStatus == "false" // Check if sharing is off
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseError", "Error loading sharing status: ${exception.message}")
        }
    }

    private fun updateSharingStatus(newStatus: String) {
        userReference.child("sharing").setValue(newStatus).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("SharingStatus", "Sharing status updated to: $newStatus")
            } else {
                Log.e("FirebaseError", "Failed to update sharing status")
            }
        }
    }

    private fun changeAppLanguage(language: String) {
        val locale = when (language) {
            "English" -> Locale.ENGLISH
            "Afrikaans" -> Locale("af")
            "Tamil" -> Locale("ta")
            "Zulu" -> Locale("zu")
            else -> Locale.ENGLISH
        }
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Restart the activity to apply the language change
        recreate()
    }

}