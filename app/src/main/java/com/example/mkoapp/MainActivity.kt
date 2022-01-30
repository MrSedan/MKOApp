package com.example.mkoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


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

    private fun sortJsonArrayByKey(arr: JSONArray): JSONArray{
        val jsonValues =  ArrayList<JSONObject>()
        for(i in 0 until arr.length()){
            jsonValues.add(arr.getJSONObject(i))
        }
        jsonValues.sortWith(compareBy { it.getInt("position") })
        return JSONArray(jsonValues)
    }
    private fun showFeelings(){
        val que = Volley.newRequestQueue(this)
        val url = "http://mskko2021.mad.hakta.pro/api/feelings"
        val sF = JsonObjectRequest(Request.Method.GET,url,null,
            {response ->
                val feel = sortJsonArrayByKey(response.getJSONArray("data"))
                val container = findViewById<LinearLayout>(R.id.container)
                for(i in 0 until feel.length()) {
                    val item = feel.getJSONObject(i)
                    val imgUrl = item.getString("image")
                    val view = layoutInflater.inflate(R.layout.feeling_card,null,false)
                    val newView = view.findViewById<ImageView>(R.id.feel_pic)
                    view.findViewById<TextView>(R.id.feel_text).text = item.getString("title")
                    //newView.setImageResource(R.drawable.ic_launcher_background)
                    Glide.with(this@MainActivity)
                        .load(imgUrl)
                        .into(newView)
                    container.addView(view)
                }
            },{ error ->
                Log.e("Aboba",error.toString())
            })
        que.add(sF)
    }
    private fun showQuotes(){
        val que = Volley.newRequestQueue(this)
        val url = "http://mskko2021.mad.hakta.pro/api/quotes"
        val sF = JsonObjectRequest(Request.Method.GET,url,null,
            { response ->
                val quotes = response.getJSONArray("data")
                val container = findViewById<LinearLayout>(R.id.quotesContainer)
                for(i in 0 until quotes.length()){
                    val item = quotes.getJSONObject(i)
                    val imgUrl = item.getString("image")
                    val view = layoutInflater.inflate(R.layout.quote_card,null,false)
                    val viewPic = view.findViewById<ImageView>(R.id.quoteImage)
                    view.findViewById<TextView>(R.id.quoteHeader).text = item.getString("title")
                    view.findViewById<TextView>(R.id.quoteText).text = item.getString("description")
                    Glide.with(this@MainActivity)
                        .load(imgUrl)
                        .into(viewPic)
                    container.addView(view)
                }
            },{error ->
                Log.e("Aboba",error.toString())
            })
        que.add(sF)
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
        showFeelings()
        showQuotes()
    }
}