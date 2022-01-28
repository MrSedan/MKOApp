package com.example.mkoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {
    private fun drawAvatar(url: String?){
        val av = findViewById<ImageView>(R.id.avatarImage)
        if (url!==null){
            Glide.with(this)
                .load(url)
                .into(av)
        } else {
            av.setImageResource(R.drawable.ic_launcher_background)
        }
        av.clipToOutline = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tokenStr = findViewById<TextView>(R.id.tokenStr)
        val s = intent.getStringExtra("token")
        val avurl = intent.getStringExtra("avatar")
        drawAvatar(avurl)
        "This is your token: $s".also { tokenStr.text = it }
        val avatar = findViewById<ImageView>(R.id.avatarImage)
        avatar.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("avatar",avurl)
            startActivity(intent)
        }
        findViewById<View>(R.id.hamburger).also {
            it.setOnClickListener{
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
        }
        findViewById<View>(R.id.musicButton).also {
            it.setOnClickListener {
                startActivity(Intent(this, MusicActivity::class.java))
            }
        }
        findViewById<View>(R.id.picturesButton).also {
            it.setOnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("avatar",avurl)
                startActivity(intent)
            }
        }
        val mainTitle = findViewById<TextView>(R.id.main_title)
        val name = getSharedPreferences("mysettings", MODE_PRIVATE).getString("name","")
        "С возвращением, $name!".also { mainTitle.text = it }
    }
}