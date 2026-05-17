package com.safecircleapp

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.Manifest


class EmergencySosActivity : AppCompatActivity() {

    private lateinit var imgbtnBack: ImageButton
    private lateinit var imgbtnSOS: ImageButton
    private val REQUEST_SMS_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_sos)

        imgbtnBack = findViewById(R.id.imgbtnBack)
        imgbtnSOS = findViewById(R.id.imgbtnSOS)

        imgbtnBack.setOnClickListener {
            // Navigate back to NavigationActivity
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Check for SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
        }

        imgbtnSOS.setOnClickListener {
            sendEmergencySms()
        }
    }

    private fun sendEmergencySms() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Reference to the user based on their userId
        val userRef = FirebaseDatabase.getInstance().getReference("Users/$userId")
        val emergencyContactsRef = FirebaseDatabase.getInstance().getReference("Users/$userId/emergency_contacts")

        // Fetch user's last known location
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(locationSnapshot: DataSnapshot) {
                val latitude = locationSnapshot.child("latitude").getValue(Double::class.java)
                val longitude = locationSnapshot.child("longitude").getValue(Double::class.java)

                if (latitude == null || longitude == null) {
                    Toast.makeText(this@EmergencySosActivity, "Location not found.", Toast.LENGTH_SHORT).show()
                    return
                }

                // Create Google Maps link
                val mapsLink = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

                // Fetch emergency contacts
                emergencyContactsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val smsManager = SmsManager.getDefault()
                            val message = "SOS! I am in an emergency situation. My location: $mapsLink"
                            var smsSentCount = 0
                            val totalContacts = dataSnapshot.childrenCount

                            for (contactSnapshot in dataSnapshot.children) {
                                val contactNumber = contactSnapshot.key // Get the contact number as the key
                                if (contactNumber != null) {
                                    try {
                                        smsManager.sendTextMessage(contactNumber, null, message, null, null)
                                        smsSentCount++
                                    } catch (e: Exception) {
                                        Toast.makeText(this@EmergencySosActivity, "Failed to send SMS to $contactNumber", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            if (smsSentCount > 0) {
                                Toast.makeText(this@EmergencySosActivity, "SOS sent to all emergency contacts!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@EmergencySosActivity, "No SMS sent. Please check contact numbers.", Toast.LENGTH_SHORT).show()
                            }

                            // Navigate back after sending the SMS
                            navigateBack()
                        } else {
                            Toast.makeText(this@EmergencySosActivity, "You have no emergency contacts.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@EmergencySosActivity, "Failed to retrieve emergency contacts.", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@EmergencySosActivity, "Failed to retrieve last location.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateBack() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS permission is required to send emergency messages.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
