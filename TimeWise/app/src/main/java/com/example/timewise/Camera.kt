package com.example.timewise

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class Camera : AppCompatActivity() {

    //variables
    lateinit var imageViewCam: ImageView
    lateinit var btnChoose : Button
    lateinit var btnTakePic:Button
    lateinit var btnUpload : Button
    //globals
    var filePath : Uri? =null
    val PICK_IMAGE_REQUEST = 22
    val storage = FirebaseStorage.getInstance()
    val storageReference = storage.reference
    val firestore = FirebaseFirestore.getInstance()
    val REQUEST_IMAGE_CAPTURE =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        imageViewCam = findViewById(R.id.imgCamera)
        btnChoose = findViewById(R.id.btnGallery)
        btnTakePic = findViewById(R.id.btnTakePic)
        btnUpload = findViewById(R.id.btnUpload)

        btnChoose.setOnClickListener{
            selectImage()
        }
        btnTakePic.setOnClickListener{
            dispatchTakePictureIntent()
        }
        btnUpload.setOnClickListener{
            uploadImage()
        }
    }

    // method to select an image
    fun selectImage() {
        val intent = Intent()
        intent.type ="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageViewCam.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewCam.setImageBitmap(imageBitmap)
            // Save the image to Firebase
            saveImageToFirebase(imageBitmap)
        }
    }

    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also{
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun saveImageUrlToFirestore(imageURL :String) {
        val imageMap = hashMapOf( "imageUrl" to imageURL)
        firestore.collection("images")
            .add(imageMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Saved online", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show() }
    }

    private fun saveImageToFirebase(imageBitmap: Bitmap) {
        // Upload image to Firebase Storage
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val imageName = UUID.randomUUID().toString() + ".jpg"
        val imageRef = storageReference.child("images/$imageName")

        imageRef.putBytes(data)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageURL = uri.toString()
                    saveImageUrlToFirestore(imageURL)
                }.addOnFailureListener { e ->
                    // Handle failure to get download URL
                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                // Handle failure to upload image
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun uploadImage() {
        filePath?.let { filePath ->
            if (contentResolver.openInputStream(filePath) != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading ...")
                progressDialog.show()
                val ref =
                    storageReference.child("images/${UUID.randomUUID()}.jpg")
                ref.putFile(filePath).addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
                    // Get the download URL of the uploaded image
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val imageURL = uri.toString()
                        // Save image URL to Firestore
                        saveImageUrlToFirestore(imageURL)
                        // Finish the Camera activity and return to HomeScreen
                        finish()
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to get URL", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred /
                            taskSnapshot.totalByteCount)
                    progressDialog.setMessage("Uploaded: ${progress.toInt()} %")
                }
            } else {
                Toast.makeText(this, "File doesn't exist", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "Camera"
    }
}
