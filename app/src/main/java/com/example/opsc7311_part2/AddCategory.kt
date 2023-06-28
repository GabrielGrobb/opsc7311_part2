package com.example.opsc7311_part2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7311_part2.databinding.ActivityAddCategoryBinding
import java.time.Duration


class AddCategory : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private var categoryCounter = ToolBox.CategoryManager.getCategoryList().size

    private var selectedColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*for(activity in ToolBox.DBManager.getActivityList()){
            println(activity)
        }*/

        //-----Color Picker-----//
        val colorPicker: TextView = findViewById(R.id.ColorPicker)
        binding.ColorPicker.setOnClickListener {
            showColorPickerDialog()
        }
        //-----Color Picker-----//

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnAddCategory.setOnClickListener {

            // Increment the categoryCounter
            categoryCounter++

            val categoryName =
                findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.txtTitle).text.toString()
            val activities = ToolBox.ActivityManager.getActivityList().toMutableList()


            val newCategory = ToolBox.CategoryDataClass(
                categoryCounter, // Increment the counter to generate a new unique ID
                categoryName,
                Duration.ZERO,
                activities,
                selectedColor
            )
            ToolBox.CategoryManager.addCategory(newCategory)

            // Return to the HomePage
            val intent = Intent(this, HomePageTest::class.java)
            startActivity(intent)


        }
    }

    private fun showColorPickerDialog() {
        val colorPickerDialog = AlertDialog.Builder(this)
        colorPickerDialog.setTitle("Pick a color")

        // Color Selection
        val colors = arrayOf(
            Color.parseColor("#9400D3"), Color.parseColor("#4B0082"),
            Color.parseColor("#0000FF"), Color.parseColor("#00FF00"), Color.parseColor("#FFFF00"),
            Color.parseColor("#FF7F00"), Color.parseColor("#FF0000")
        )

        val colorAdapter = ColorAdapter(this, colors)

        colorPickerDialog.setAdapter(colorAdapter) { _, which ->
            selectedColor = colors[which]
            // Set the selected color to the colorPicker TextView
            binding.chosenColor.setBackgroundColor(selectedColor)
        }

        colorPickerDialog.show()
    }

    inner class ColorAdapter(private val context: Context, private val colors: Array<Int>) :
        ArrayAdapter<Int>(context, 0, colors) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val colorView = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_color, parent, false)
            val color = colors[position]
            val colorPreview = colorView.findViewById<View>(R.id.colorPreview)
            colorPreview.setBackgroundColor(color)
            return colorView
        }
    }
}

