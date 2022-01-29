package com.example.mkoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import org.json.JSONObject


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
    private fun showFeelings(){
        val url = "https://mskko2021.mad.hakta.pro/api/feelings"
        val sF = JsonObjectRequest(Request.Method.GET,url,null,
            {response ->

            },{})
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val avurl = intent.getStringExtra("avatar")
        drawAvatar(avurl)
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