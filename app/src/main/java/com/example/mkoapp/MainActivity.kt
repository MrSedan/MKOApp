package com.example.mkoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.toolbox.StringRequest
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val but: Button = findViewById(R.id.button)
        val tex: TextView = findViewById(R.id.textView)
        val que = Volley.newRequestQueue(this)
        but.setOnClickListener {
            val url = "http://mskko2021.mad.hakta.pro/api/quotes"

            val strReq = StringRequest(Request.Method.GET,url,
                { response ->
                    val ob = JSONObject(response).getJSONArray("data")
                    for (i in 0 until ob.length()){
                        val item = ob.getJSONObject(i)
                        Log.d("Aboba",item.toString())
                    }

                },
                { error -> Log.e("Aboba", error.toString()) }
            )
            que.add(strReq)

        }
    }
}