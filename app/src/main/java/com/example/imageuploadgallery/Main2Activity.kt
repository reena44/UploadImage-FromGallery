package com.example.imageuploadgallery

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission_group.CAMERA
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main2.*


class Main2Activity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE: Int = 12
    private val IMAGE_PICKER_SELECT: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        btn_vdo.setOnClickListener {
            uploadVideo()
        }

}


    @SuppressLint("IntentReset")
    private fun uploadVideo() {
        val pickIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );
            pickIntent.type = "image/* video/*"
            startActivityForResult(pickIntent, IMAGE_PICKER_SELECT)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val selectedMediaUri = data!!.data
            if (selectedMediaUri.toString().contains("image")) {

                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, selectedMediaUri)
                img.setImageBitmap(bitmap)
                //handle image
            } else if (selectedMediaUri.toString().contains("video")) {
                val mediaController = MediaController(this)
                vdovew.setVideoURI(selectedMediaUri!!)
                mediaController.setAnchorView(vdovew)
                mediaController.setMediaPlayer(vdovew)
                vdovew.setMediaController(mediaController)
                vdovew.start()

                //handle video
            }
        }
    }
}
