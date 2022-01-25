package com.example.mkoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
        ed.putString("token",text)
        ed.apply()
    }
    private fun getText(): String? {
        val ed = getPreferences(MODE_PRIVATE)
        return ed.getString("token", "")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val but: Button = findViewById(R.id.button)
        val tex: TextView = findViewById(R.id.token)
        val que = Volley.newRequestQueue(this)
        val tok = getText()
        if (tok!=null) tex.text = tok
        Log.d("Aboba","Created")
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
            jsonBody.put("email","junior@wsr.ru")
            jsonBody.put("password","junior")
            val strReq = JsonObjectRequest(Request.Method.POST, url, jsonBody,
                { response ->
                    val s = response.get("token").toString()
                    val avatar = response.get("avatar").toString()
                    "Your token is $s".also { tex.text = it }
                    val intent = Intent(this, ProfileActivity::class.java)
                    setText(s)
                    intent.putExtra("token",s)
                    intent.putExtra("avatar",avatar)
                    startActivity(intent)
                },
                {
                    error -> Log.e("Aboba", error.toString())
                })
            que.add(strReq)
        }

    }
}