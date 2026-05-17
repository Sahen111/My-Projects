package com.safecircleapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class NameActivity : AppCompatActivity() {



    private lateinit var email: String
    private lateinit var password: String
    private lateinit var edtName: EditText
    private lateinit var circleimageview: CircleImageView
    private var resultUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        edtName = findViewById(R.id.edtName)
        circleimageview = findViewById(R.id.circleImageView)

        circleimageview.setOnClickListener {
            // Open image picker
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 12)
        }

        val myIntent = intent
        if (myIntent != null) {
            email = myIntent.getStringExtra("email") ?: ""
            password = myIntent.getStringExtra("password") ?: ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                resultUri = imageUri // Store the selected image URI
                circleimageview.setImageURI(resultUri) // Display the selected image

                // Extract the filename from the image URI
                val imageName = imageUri.lastPathSegment?.substringAfterLast('/') ?: "temp_image"
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "temp_user"
                val storageReference = FirebaseStorage.getInstance().getReference("ProfileImages/$imageName.jpg")

                // Upload the image to Firebase Storage
                val uploadTask = storageReference.putFile(resultUri!!)

                uploadTask.addOnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        // Get the download URL
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            imageUrl = uri.toString()  // Store the image URL
                        }
                    } else {
                        Toast.makeText(this, "Image upload failed: ${uploadTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun generatecode(v: View) {
        val name = edtName.text.toString()

        // Error handling for empty name field
        if (name.isEmpty()) {
            edtName.error = "Name is required"
            edtName.requestFocus()
            return
        }

        // Error handling for not choosing an image
        if (imageUrl == null) {
            Toast.makeText(this, "Please choose an image, or wait for your current image to upload.", Toast.LENGTH_SHORT).show()
            return
        }

        // If all validations pass, generate the invite code and proceed
        val myDate = Date()
        val format1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
        val date = format1.format(myDate)

        val r = Random()
        val n = 100000 + r.nextInt(900000)
        val code = n.toString()

        val myIntent = Intent(this@NameActivity, InviteCodeActivity::class.java)
        myIntent.putExtra("name", name)
        myIntent.putExtra("email", email)
        myIntent.putExtra("password", password)
        myIntent.putExtra("date", date)
        myIntent.putExtra("isSharing", "false")
        myIntent.putExtra("code", code)
        myIntent.putExtra("imageUrl", imageUrl)  // Pass the image URL
        startActivity(myIntent)
        finish()
    }
}