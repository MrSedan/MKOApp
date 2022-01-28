package com.example.mkoapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.schedule
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*


class SplashActivity : AppCompatActivity() {
    lateinit var mSettings: SharedPreferences
    private fun getText(key:String): String? {
        return mSettings.getString(key,"")
    }
    private fun goMain(){
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
    private fun tryLogin(){
        val email = getText("email")
        val que = Volley.newRequestQueue(this)
        val pass = getText("pass")
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", pass)
        val url = "http://mskko2021.mad.hakta.pro/api/user/login"
        val strReq = JsonObjectRequest(Request.Method.POST, url, jsonBody,
            { response ->
                val s = response.get("token").toString()
                val avatar = response.get("avatar").toString()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("token", s)
                intent.putExtra("avatar", avatar)
                startActivity(intent)
                finishAffinity()
            },{
                goMain()
            })
        Timer().schedule(1000){
            que.add(strReq)
            que.start()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = getSharedPreferences("mysettings", MODE_PRIVATE);
        tryLogin()
        setContentView(R.layout.activity_splash)
    }
}