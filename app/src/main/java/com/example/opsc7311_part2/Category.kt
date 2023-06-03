package com.example.opsc7311_part2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.opsc7311_part2.databinding.ActivityCategoryBinding
import com.example.opsc7311_part2.databinding.ActivityHomePageTestBinding

class Category : AppCompatActivity()
{
    private lateinit var binding: ActivityCategoryBinding
    var tollieBox = ToolBox()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cattxt = findViewById<TextView>(R.id.CategoryName)

        cattxt.text = ToolBox.categoryName
        //ToolBox.categoryName =""

        /*category.categoryName = intent.getStringExtra("categoryName").toString()
        //binding.tv_PlacedOrder.text = category.categoryName
        binding.tvPlacedOrder.text = category.categoryName

        when(category.categoryName)
        {

        }*/

        /*val imageResource = intent.getIntExtra("imageResource", 0)

        // Use the retrieved data to display the category details
        val categoryImageView = findViewById<ImageView>(R.id.IconPicture)
        val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)

        *//*categoryImageView.setImageResource(imageResource)
        categoryNameTextView.text = categoryName*/
    }
}