package com.example.mkoapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var mSettings: SharedPreferences
    private fun setText(key: String,text: String){
        val ed = mSettings.edit()
        ed.putString(key,text)
        ed.apply()
    }
    private fun getText(key:String): String? {
        return mSettings.getString(key,"")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = getSharedPreferences("mysettings", MODE_PRIVATE);
        setContentView(R.layout.activity_login)
        val but: ImageButton = findViewById(R.id.signInButton)
        val que = Volley.newRequestQueue(this)
        val email = findViewById<EditText>(R.id.emailField)
        val em = getText("email")
        findViewById<TextView>(R.id.register).also {
            it.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        if(em!==null) email.setText(em)
        Log.d("Aboba","Created")
        findViewById<ConstraintLayout>(R.id.main).also {
            it.setOnTouchListener(object : OnSwipeTouchListener(this@LoginActivity){
                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    Log.d("Aboba","Swipe left")
                }
                override fun onSwipeRight() {
                    super.onSwipeRight()
                    Log.d("Aboba","Swipe right")
                }
                override fun onSwipeUp() {
                    super.onSwipeUp()
                    Log.d("Aboba","Swipe up")
                }
                override fun onSwipeDown() {
                    super.onSwipeDown()
                    Log.d("Aboba","Swipe down")
                }

                override fun onLongClick() {
                    super.onLongClick()
                    Toast.makeText(applicationContext,"Long click",Toast.LENGTH_SHORT).show()
                }
            })
        }
        but.setOnClickListener {
            val url = "http://mskko2021.mad.hakta.pro/api/user/login"
            val jsonBody = JSONObject()
            val pass = findViewById<EditText>(R.id.passField)
            if (email.text.contains('@') && pass.length()>0) {
                jsonBody.put("email", email.text)
                jsonBody.put("password", pass.text)
                val strReq = JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    { response ->
                        val s = response.get("token").toString()
                        val avatar = response.get("avatar").toString()
                        val intent = Intent(this, ProfileActivity::class.java)
                        setText("email",response.getString("email"))
                        setText("pass",pass.text.toString())
                        intent.putExtra("token", s)
                        intent.putExtra("avatar", avatar)
                        startActivity(intent)
                        finish()
                    },
                    { error ->
                        if (error.networkResponse.statusCode == 470 ){
                            val err = findViewById<TextView>(R.id.errField)
                            "Incorrect email or password!".also { err.text = it }
                        }else Log.e("Aboba", error.toString())
                    })
                que.add(strReq)
            } else {
                val err = findViewById<TextView>(R.id.errField)
                "Type email and password, please!".also { err.text = it }
            }
        }

    }
}