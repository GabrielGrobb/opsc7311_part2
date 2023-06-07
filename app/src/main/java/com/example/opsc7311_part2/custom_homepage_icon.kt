package com.example.opsc7311_part2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*

class custom_homepage_icon @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs) {

            private var categoryID:Int = 0;
            private var categoryName: String = ""
            private var containerBackground: Int =0

        init {
            LayoutInflater.from(context).inflate(R.layout.activity_custom_homepage_icon, this, true)

            val relContainer = findViewById<RelativeLayout>(R.id.relContainer)
            relContainer.setOnClickListener {
                // Handle the click event for the RelativeLayout here
                // You can perform any actions you want when the RelativeLayout is clicked

                val intent = Intent(context, Category::class.java)
                intent.putExtra("categoryName", categoryName)
                intent.putExtra("categoryID", categoryID)
                context.startActivity(intent)

            }

        }

        fun setCatName(name: String)
        {
            val catName = findViewById<TextView>(R.id.categoryName)
            catName.text = name
            categoryName = name

        }

        fun setCatBackground(background: Int) {
            val catBackground = findViewById<RelativeLayout>(R.id.relContainer)
            catBackground.setBackgroundColor(background)
            containerBackground = background
        }

        fun setCatID(ID: Int) {
            //categoryID = ID+1 // Increment the counter by 1
            val catID = findViewById<TextView>(R.id.categoryID)
            catID.text = ID.toString()
            categoryID = ID
        }
}
