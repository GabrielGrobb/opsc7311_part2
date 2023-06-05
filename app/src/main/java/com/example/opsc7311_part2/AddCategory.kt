package com.example.opsc7311_part2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.opsc7311_part2.R.id.datePickerButton
import com.example.opsc7311_part2.databinding.ActivityAddCategoryBinding


class AddCategory : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private var categoryCounter = ToolBox.CategoryManager.getCategoryList().size
    //private var catCounter=1;
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnAddCategory.setOnClickListener {

            // Increment the categoryCounter
            categoryCounter++

            val categoryName = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtTitle).text.toString()
            val categoryIcon = findViewById<ImageView>(R.id.categoryImage)
            val activities = ToolBox.ActivityManager.getActivityList().toMutableList()
            //val categoryColor = findViewById<TextView>(R.id.txtTitle)

            val newCategory = ToolBox.CategoryDataClass(
                categoryCounter, // Increment the counter to generate a new unique ID
                categoryName,
                categoryIcon,
                activities
                //categoryColor
            )
            ToolBox.CategoryManager.addCategory(newCategory)


            // Return to the HomePage
            val intent = Intent(this, HomePageTest::class.java)
            startActivity(intent)
        }

    }




}