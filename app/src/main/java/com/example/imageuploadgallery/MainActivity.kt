package com.example.imageuploadgallery

import android.R.attr
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private var filePath: Uri? = null
    private lateinit var storageReference: StorageReference
    private lateinit var storage: FirebaseStorage
    private val RESULT_LOAD_IMG: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = FirebaseStorage.getInstance();
        storageReference = storage.reference;
        butn.setOnClickListener {

            imageChooser()
        }
        btnUpload.setOnClickListener {
            uploadImage()
        }

           // val photoPickerIntent = Intent(Intent.ACTION_PICK)
          //  photoPickerIntent.type = "image/*"
            //startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG)
        }

    private fun imageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {

                if (resultCode == Activity.RESULT_OK
                ) {
                    if (data != null) {
                        filePath = data.data
                    }
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                        img.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()


            }
        }
    }

                /*
                    val imageUri: Uri? = data?.data
                    val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    img.setImageBitmap(selectedImage)
                    } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                    } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
                    }
                    */


    private fun uploadImage() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()
            val ref =
                storageReference.child("images/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
        }
    }
}

