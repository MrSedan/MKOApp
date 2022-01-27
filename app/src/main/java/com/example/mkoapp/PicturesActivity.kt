package com.example.mkoapp

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide

class PicturesActivity : AppCompatActivity() {
    private val REQUEST_CODE = 100
    private fun drawAvatar(url: String?){
        val av = findViewById<ImageView>(R.id.avatarImgPic)
        if (url!==null){
            Glide.with(this)
                .load(url)
                .into(av)
        } else {
            av.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val pic = ImageView(this)
            val layout = findViewById<ConstraintLayout>(R.id.pictureLayout)
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            pic.setImageURI(data?.data)
            layout.addView(pic,layoutParams)
        }

    }
    private fun openGalleryForImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_CODE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pictures)
        val avurl = intent.getStringExtra("avatar")
        drawAvatar(avurl)
        val addPictureButton = findViewById<Button>(R.id.addPictureButton)
        addPictureButton.setOnClickListener {
            openGalleryForImage()
        }
    }
}