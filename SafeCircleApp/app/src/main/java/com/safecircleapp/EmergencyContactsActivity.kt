package com.safecircleapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class EmergencyContactsActivity : AppCompatActivity() {

    private lateinit var recyclerViewContacts: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var contactsAdapter: ContactsAdapter
    private val selectedContacts = mutableSetOf<String>()
    private val contactList: MutableList<Contact> = ArrayList() // Original contact list
    private lateinit var filteredContactsList: MutableList<Contact> // Filtered contact list
    private lateinit var imgbtnBack: ImageButton

    companion object {
        private const val REQUEST_CONTACTS_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contacts)

        recyclerViewContacts = findViewById(R.id.recyclerviewContacts)
        searchBar = findViewById(R.id.edtsearchBar)
        imgbtnBack = findViewById(R.id.imgbtnBack)

        recyclerViewContacts.layoutManager = LinearLayoutManager(this)

        loadSelectedContacts()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS_PERMISSION)
        } else {
            loadContacts()
        }

        imgbtnBack.setOnClickListener {
            // Navigate back to SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Search functionality
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadSelectedContacts() {
        val sharedPreferences = getSharedPreferences("EmergencyContacts", MODE_PRIVATE)
        val selected = sharedPreferences.getStringSet("selectedContacts", setOf()) ?: setOf()
        selectedContacts.addAll(selected)
    }

    // Save selected contacts to shared preferences
    private fun saveSelectedContacts() {
        val sharedPreferences = getSharedPreferences("EmergencyContacts", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("selectedContacts", selectedContacts)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveSelectedContacts() // Save selected contacts when activity is destroyed
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()  // Load contacts if permission is granted
            } else {
                Toast.makeText(this, "Contacts permission is required to add emergency contacts.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadContacts() {

        val contentResolver = contentResolver
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)

        cursor?.let {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            if (nameIndex != -1 && numberIndex != -1) {
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    var number = it.getString(numberIndex)

                    // Format the phone number
                    number = formatPhoneNumber(number)

                    contactList.add(Contact(name, number))
                }
            } else {
                Toast.makeText(this, "Unable to fetch contacts. Column not found.", Toast.LENGTH_SHORT).show()
            }
            it.close()
        }

        if (contactList.isNotEmpty()) {
            filteredContactsList = contactList.toMutableList() // Initialize filtered list
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "" // Get the user ID from Firebase Authentication
            contactsAdapter = ContactsAdapter(filteredContactsList, userId, selectedContacts)
            recyclerViewContacts.adapter = contactsAdapter
        } else {
            Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterContacts(query: String) {
        filteredContactsList.clear()
        if (query.isEmpty()) {
            filteredContactsList.addAll(contactList) // Show all contacts if query is empty
        } else {
            for (contact in contactList) {
                if (contact.name.contains(query, ignoreCase = true) ||
                    contact.number.contains(query, ignoreCase = true)) {
                    filteredContactsList.add(contact) // Add matching contacts
                }
            }
        }
        contactsAdapter.notifyDataSetChanged()
    }

    private fun formatPhoneNumber(number: String): String {
        // Remove any non-digit characters
        val cleanedNumber = number.replace(Regex("[^\\d]"), "")

        // Check if the number starts with 27
        return if (cleanedNumber.startsWith("27")) {
            // Remove '27' and add '0' at the start
            "0${cleanedNumber.substring(2)}"
        } else {
            cleanedNumber
        }
    }

}