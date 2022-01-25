package com.example.mkoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.io.InputStream
import java.net.URL


class ProfileActivity : AppCompatActivity() {
    private fun drawAvatar(url: String?){
        val av = findViewById<ImageView>(R.id.avatarImage)
        if (url!==null){
            Glide.with(this)
                .load(url)
                .into(av)
        } else {
            av.setImageResource(R.drawable.ic_launcher_background)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val tokenStr = findViewById<TextView>(R.id.tokenStr)
        val s = intent.getStringExtra("token")
        val avurl = intent.getStringExtra("avatar")
        drawAvatar(avurl)
        "This is your token: $s".also { tokenStr.text = it }
    }
}