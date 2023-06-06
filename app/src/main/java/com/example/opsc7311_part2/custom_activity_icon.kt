package com.example.opsc7311_part2

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*

class custom_activity_icon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs) {

    private var activityID:Int = 0;
    private var activityName: String = ""
    private var imageResource: Bitmap? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_custom_icon, this, true)

        val relContainer = findViewById<RelativeLayout>(R.id.relContainer)
        relContainer.setOnClickListener {
            // Handle the click event for the RelativeLayout here
            // You can perform any actions you want when the RelativeLayout is clicked

            val intent = Intent(context, CaptureTask::class.java)
            intent.putExtra("activityName", activityName)
            intent.putExtra("activityID", activityID)
            intent.putExtra("imageIcon",imageResource)
            context.startActivity(intent)

        }

    }
    // Perform any initialization or customization here
    // You can access and modify the views within the custom component layout

    fun setIcon(bitmap: Bitmap)
    {
        val img = findViewById<ImageView>(R.id.ActivityIcon)

        img.setImageBitmap(bitmap)

        imageResource = bitmap

    }

    fun setActName(name: String)
    {
        val catName = findViewById<TextView>(R.id.txtActivityName)
        catName.text = name
        activityName = name

    }

    fun setActID(ID: Int) {
        //categoryID = ID+1 // Increment the counter by 1
        val actID = findViewById<TextView>(R.id.activityID)
        actID.text = ID.toString()
        activityID = ID
    }

}