package com.example.mkoapp

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class ProfileActivity : AppCompatActivity() {
    lateinit var mSettings: SharedPreferences
    private val REQUEST_CODE = 1

    private fun setText(key: String,text: String){
        val ed = mSettings.edit()
        ed.putString(key,text)
        ed.apply()
    }

    //Draw user avatar
    private fun drawAvatar(url: String?){
        val av = findViewById<ImageView>(R.id.avatarImgPic)
        if (url!==null){
            Glide.with(this)
                .load(url)
                .into(av)
        } else {
            av.setImageResource(R.drawable.ic_launcher_background)
        }
        av.clipToOutline = true
    }

    //TODO: add user images
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val picView = layoutInflater.inflate(R.layout.picture_card,null)
            val pic = picView.findViewById<ImageView>(R.id.picture)
            val layout = findViewById<GridLayout>(R.id.pictureContainer)
            /*val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )*/
            pic.setImageURI(data?.data)
            val imageURI = data?.data
            val hours = Calendar.getInstance().time.hours.toString()
            val mins = Calendar.getInstance().time.minutes.toString()
            val time =  when(mins.toInt()){
                in 0..9-> "$hours:0$mins"
                else -> "$hours:$mins"
            }
            picView.findViewById<TextView>(R.id.pictureTime).text = time
            layout.addView(picView)
            val picString = mSettings.getString("pictures","")

            var picArr = JSONArray()
            if (picString != ""){
                picArr = JSONArray(picString)
            }
            this.grantUriPermission(this.packageName, imageURI, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            if (imageURI != null) {
                this.contentResolver.takePersistableUriPermission(imageURI, takeFlags)
            }
            val imageUriStr = imageURI.toString()
            val jsonValues = ArrayList<JSONObject>()
            for (i in 0 until picArr.length())
                jsonValues.add(picArr.getJSONObject(i))
            val newPicJson = "{\"time\":\"$time\",\"uri\":\"$imageUriStr\"}"
            jsonValues.add(JSONObject(newPicJson))
            picArr = JSONArray(jsonValues)
            setText("pictures",picArr.toString())


            //Add new button and delete previous
            var picB = layout.findViewById<ConstraintLayout>(R.id.pictureBCard)
            layout.removeView(picB)
            val newPicB = layoutInflater.inflate(R.layout.picture_button,null)
            layout.addView(newPicB)
            picB = layout.findViewById(R.id.pictureBCard)
            val picBCard = picB.findViewById<Button>(R.id.pictureButton)
            picBCard.setOnClickListener {
                openGalleryForImage()
            }
            layout.getChildAt(picArr.length()-1).setOnClickListener {
                finish()
                val intent = Intent(this,PhotoActivity::class.java)
                intent.putExtra("id",picArr.length()-1).putExtra("pic",imageUriStr)
                startActivity(intent)
            }

        }

    }
    private fun openGalleryForImage(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent,REQUEST_CODE)
    }

    private fun clickListen(){
        findViewById<View>(R.id.home).also {
            it.setOnClickListener {
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
        findViewById<View>(R.id.musicButton).also {
            it.setOnClickListener {
                startActivity(Intent(this, MusicActivity::class.java))
            }
        }
        findViewById<View>(R.id.hamburger).also {
            it.setOnClickListener {
                startActivity(Intent(this,MenuActivity::class.java))
            }
        }
        findViewById<TextView>(R.id.exit).also {
            it.setOnClickListener {
                val ed = mSettings.edit()
                ed.putString("pass","")
                ed.apply()
                startActivity(Intent(this,OnBoardingActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun addPictures(){
        val picString = mSettings.getString("pictures","")

        var pic = JSONArray()
        if (picString != ""){
            pic = JSONArray(mSettings.getString("pictures",""))
        }
        val container = findViewById<GridLayout>(R.id.pictureContainer)
        for(i in 0 until pic.length()){
            val item: JSONObject = pic.getJSONObject(i)
            val newView = layoutInflater.inflate(R.layout.picture_card,null)
            val picture = newView.findViewById<ImageView>(R.id.picture)
            val picTime = newView.findViewById<TextView>(R.id.pictureTime)
            picture.setImageURI(Uri.parse(item.getString("uri")))
            picTime.text = item.getString("time")
            container.addView(newView)
            container.getChildAt(i).setOnClickListener {
                it.clipToOutline = true
                finish()
                val intent = Intent(this,PhotoActivity::class.java)
                intent.putExtra("id",i).putExtra("pic",item.getString("uri"))
                startActivity(intent)
            }
        }
        val picB = layoutInflater.inflate(R.layout.picture_button,null)
        container.addView(picB)
        val picButton = container.findViewById<Button>(R.id.pictureButton)
        picButton.setOnClickListener {
            openGalleryForImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = getSharedPreferences("mysettings", MODE_PRIVATE)
        setContentView(R.layout.activity_profile)

        //Draw avatar
        val avurl = intent.getStringExtra("avatar")
        drawAvatar(avurl)

        //Set name in activity
        findViewById<TextView>(R.id.name).also {
            val name = mSettings.getString("name","")
            it.text = name
        }
        addPictures()
        clickListen()
    }
}