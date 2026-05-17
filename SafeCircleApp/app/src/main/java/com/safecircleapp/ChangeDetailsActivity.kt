package com.safecircleapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class ChangeDetailsActivity : AppCompatActivity() {

    private lateinit var imageViewProfile: CircleImageView
    private lateinit var editTextName: EditText
    private lateinit var buttonChangePicture: Button
    private lateinit var buttonSave: Button
    private lateinit var imgbtnBack: ImageButton

    private var selectedImageUri: Uri? = null

    // Create a reference to Firebase Storage
    private lateinit var storageReference: StorageReference

    private val requestImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it // Save the selected image URI
            imageViewProfile.setImageURI(it)  // Set the selected image to ImageView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_details)

        imageViewProfile = findViewById(R.id.imageViewProfile)
        editTextName = findViewById(R.id.editTextName)
        buttonChangePicture = findViewById(R.id.buttonChangePicture)
        buttonSave = findViewById(R.id.buttonSave)
        imgbtnBack = findViewById(R.id.imgbtnBack)

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().reference.child("profile_pictures")

        buttonChangePicture.setOnClickListener {
            requestImageLauncher.launch("image/*")  // Launch the image picker
        }

        imgbtnBack.setOnClickListener {
            // Navigate back to SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonSave.setOnClickListener {
            saveChanges()
            // Navigate back to SettingsActivity
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Load current user data to prefill the fields
        loadCurrentUserData()
    }

    private fun loadCurrentUserData() {
        // Load user's current name and profile picture URL from Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                    editTextName.setText(name)
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this@ChangeDetailsActivity)
                            .load(imageUrl)
                            .into(imageViewProfile)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun saveChanges() {
        val newName = editTextName.text.toString().trim()

        // Check if the name is empty
        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be left empty.", Toast.LENGTH_SHORT).show()
            return // Exit the function if the name is empty
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)

            // Save the name in Firebase Realtime Database
            userReference.child("name").setValue(newName)

            // Check if a new profile picture was selected
            if (selectedImageUri != null) {
                // Create a unique file name for the profile picture
                val fileName = "${currentUser.uid}_${UUID.randomUUID()}.jpg"
                val fileRef = storageReference.child(fileName)

                // Upload the selected image to Firebase Storage
                fileRef.putFile(selectedImageUri!!)
                    .addOnSuccessListener {
                        // Get the download URL of the uploaded image
                        fileRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Update the imageUrl in the Firebase Realtime Database
                            userReference.child("imageUrl").setValue(imageUrl)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Changes saved successfully.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Failed to save changes.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Changes saved successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}