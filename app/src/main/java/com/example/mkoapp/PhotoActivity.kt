package com.example.mkoapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray
import java.net.URI

class PhotoActivity : AppCompatActivity() {
    lateinit var mSettings: SharedPreferences
    private fun deletePhotoById(id:Int){
        val pictures = JSONArray(mSettings.getString("pictures",""))
        pictures.remove(id)
        mSettings.edit().remove("pictures").putString("pictures",pictures.toString())
            .apply()
    }
    private fun drawPhoto(){
        val uri = intent.getStringExtra("pic")
        val pic = findViewById<ImageView>(R.id.photo)
        pic.setImageURI(Uri.parse(uri))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = getSharedPreferences("mysettings", MODE_PRIVATE)
        setContentView(R.layout.activity_photo)
        val id = intent.getIntExtra("id",-1)
        drawPhoto()
        findViewById<TextView>(R.id.delete).setOnClickListener{
            if (id!=-1){
                deletePhotoById(id)
                finish()
                val intent = Intent(this,ProfileActivity::class.java)
                val avurl = mSettings.getString("avatar","")
                intent.putExtra("avatar",avurl)
                startActivity(intent)
            }
        }
        findViewById<TextView>(R.id.close).setOnClickListener {
            finish()
            val intent = Intent(this,ProfileActivity::class.java)
            val avurl = mSettings.getString("avatar","")
            intent.putExtra("avatar",avurl)
            startActivity(intent)
        }
        val listener =object: OnSwipeTouchListener(this@PhotoActivity){
            override fun onSwipeRight() {
                super.onSwipeRight()
                Log.d("Aboba","Swipe right")
                finish()
                val intent = Intent(this@PhotoActivity,ProfileActivity::class.java)
                val avurl = mSettings.getString("avatar","")
                intent.putExtra("avatar",avurl)
                startActivity(intent)
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                if (id!=-1){
                    deletePhotoById(id)
                    finish()
                    val intent = Intent(this@PhotoActivity,ProfileActivity::class.java)
                    val avurl = mSettings.getString("avatar","")
                    intent.putExtra("avatar",avurl)
                    startActivity(intent)
                }
            }

            override fun onDoubleClick() {
                super.onDoubleClick()
                val pic = findViewById<ImageView>(R.id.photo)
                if (pic.scaleX == 2F){
                    pic.scaleX = 1F
                    pic.scaleY = 1F
                } else {
                    pic.scaleX = 2F
                    pic.scaleY = 2F
                }
                pic.requestLayout()
            }
        }
        findViewById<ImageView>(R.id.photo)
            .also{
                it.setOnTouchListener(listener)
            }
    }
}