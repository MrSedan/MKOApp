package com.example.mkoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

    private fun setText(text: String){
        val ed = getPreferences(MODE_PRIVATE).edit()
        ed.putString("email",text)
        ed.apply()
    }
    private fun getText(): String? {
        val ed = getPreferences(MODE_PRIVATE)
        return ed.getString("email", "")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val but: Button = findViewById(R.id.button)
        val que = Volley.newRequestQueue(this)
        val email = findViewById<EditText>(R.id.emailField)
        val em = getText()
        if(em!==null) email.setText(em)
        Log.d("Aboba","Created")
        val main = findViewById<ConstraintLayout>(R.id.main).also {
            it.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity){
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
            })
        }
        but.setOnClickListener {
            /*val url = "http://mskko2021.mad.hakta.pro/api/quotes"

            val strReq = StringRequest(Request.Method.GET,url,
                { response ->
                    val ob = JSONObject(response).getJSONArray("data")
                    for (i in 0 until ob.length()){
                        val item = ob.getJSONObject(i)
                        Log.d("Aboba",item.toString())
                    }

                },
                { error -> Log.e("Aboba", error.toString()) }
            )*/

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
                        setText(response.getString("email"))
                        intent.putExtra("token", s)
                        intent.putExtra("avatar", avatar)
                        startActivity(intent)

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