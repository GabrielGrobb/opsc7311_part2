package com.example.opsc7311_part2

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*

class custom_activity_icon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs) {

    private var activityName: String = ""
    //private var imageResource: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_custom_icon, this, true)

        val relContainer = findViewById<RelativeLayout>(R.id.relContainer)
        relContainer.setOnClickListener {
            // Handle the click event for the RelativeLayout here
            // You can perform any actions you want when the RelativeLayout is clicked

            val intent = Intent(context, CaptureTask::class.java)
            intent.putExtra("activityName", activityName)
            //intent.putExtra("imageIcon",imageResource)
            context.startActivity(intent)

        }

    }
    // Perform any initialization or customization here
    // You can access and modify the views within the custom component layout

    fun setIcon(imgResource: Int)
    {
        val img = findViewById<ImageView>(R.id.ActivityIcon)

        img.setImageResource(imgResource)

        //imageResource = imgResource

    }

    fun setActName(name: String)
    {
        val catName = findViewById<TextView>(R.id.txtActivityName)
        catName.text = name
        activityName = name

    }

}