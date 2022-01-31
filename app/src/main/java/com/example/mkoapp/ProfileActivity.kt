package com.example.mkoapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide

class ProfileActivity : AppCompatActivity() {
    lateinit var mSetteings: SharedPreferences
    private val REQUEST_CODE = 100

    //Draw user avatar
    private fun drawAvatar(url: String?){
        val av = findViewById<ImageView>(R.id.avatarImgPic)
        if (url!==null){
            Glide.with(this)
                .load(url)
                .into(av)
        } else {
            av.setImageResource(R.drawable.ic_launcher_background)
        }
        av.clipToOutline = true
    }

    //TODO: add user images
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

    private fun clickListen(){
        findViewById<View>(R.id.home).also {
            it.setOnClickListener {
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
        findViewById<View>(R.id.musicButton).also {
            it.setOnClickListener {
                startActivity(Intent(this, MusicActivity::class.java))
            }
        }
        findViewById<View>(R.id.hamburger).also {
            it.setOnClickListener {
                startActivity(Intent(this,MenuActivity::class.java))
            }
        }
        findViewById<TextView>(R.id.exit).also {
            it.setOnClickListener {
                val ed = mSetteings.edit()
                ed.putString("pass","")
                ed.apply()
                startActivity(Intent(this,OnBoardingActivity::class.java))
                finishAffinity()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSetteings = getSharedPreferences("mysettings", MODE_PRIVATE)
        setContentView(R.layout.activity_profile)

        //Draw avatar
        val avurl = intent.getStringExtra("avatar")
        drawAvatar(avurl)

        //Set name on activity
        findViewById<TextView>(R.id.name).also {
            val name = mSetteings.getString("name","")
            it.text = name
        }

        clickListen()
    }
}