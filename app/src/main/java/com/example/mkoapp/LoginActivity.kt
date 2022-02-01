package com.example.mkoapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var mSettings: SharedPreferences
    //Save string to preferences
    private fun setText(key: String,text: String){
        val ed = mSettings.edit()
        ed.putString(key,text)
        ed.apply()
    }

    //Get string from preferences
    private fun getText(key:String): String? {
        return mSettings.getString(key,"")
    }

    //Login
    private fun login(email: String){
        val que = Volley.newRequestQueue(this)
        val url = "http://mskko2021.mad.hakta.pro/api/user/login"
        val jsonBody = JSONObject()
        val pass = findViewById<EditText>(R.id.passField)
        if (email.contains('@') && pass.length()>0) {
            jsonBody.put("email", email)
            jsonBody.put("password", pass.text)
            val strReq = JsonObjectRequest(Request.Method.POST, url, jsonBody,
                { response ->
                    val s = response.get("token").toString()
                    val avatar = response.get("avatar").toString()
                    val intent = Intent(this, MainActivity::class.java)
                    setText("email",response.getString("email"))
                    setText("pass",pass.text.toString())
                    setText("name",response.getString("nickName"))
                    setText("avatar",avatar)
                    intent.putExtra("token", s)
                    intent.putExtra("avatar", avatar)
                    startActivity(intent)
                    finishAffinity()
                },
                { error ->
                    if (error.networkResponse.statusCode == 470 ){
                        Toast.makeText(this,"Incorrect email or password!",Toast.LENGTH_SHORT).show()
                    }else Log.e("Aboba", error.toString())
                })
            que.add(strReq)
        } else {
            Toast.makeText(this, "Type email and password, please!",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = getSharedPreferences("mysettings", MODE_PRIVATE);
        setContentView(R.layout.activity_login)
        val but: ImageButton = findViewById(R.id.signInButton)
        val email = findViewById<EditText>(R.id.emailField)
        val em = getText("email")
        findViewById<TextView>(R.id.register).also {
            it.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        //Set email field text from saved preferences
        if(em!==null) email.setText(em)

        Log.d("Aboba","Created")
        but.setOnClickListener {
            login(email.text.toString())
        }
    }
}