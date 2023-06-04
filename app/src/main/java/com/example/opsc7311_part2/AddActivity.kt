package com.example.opsc7311_part2

//Add necessary references for view
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7311_part2.databinding.ActivityAddActivityBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddActivityBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        val categoryNames = ToolBox.CategoryManager.getCategoryList()

        val txtCategory = findViewById<AutoCompleteTextView>(R.id.txtCategory)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryNames)
        txtCategory.setAdapter(adapter)

        binding.btnAddActivity.setOnClickListener {
            val actTitle = findViewById<TextInputEditText>(R.id.txtTitle).text.toString()
            val actClient = findViewById<TextInputEditText>(R.id.txtClient).text.toString()
            val actLocation = findViewById<TextInputEditText>(R.id.txtLocation).text.toString()
            val actCategoryName = findViewById<AutoCompleteTextView>(R.id.txtCategory).text.toString()
            val actCategory = getCategoryByName(actCategoryName)
            val actDuration = findViewById<TextInputEditText>(R.id.txtDuration).text.toString().toDouble()
            val actStartDate = parseDatePicker(findViewById<DatePicker>(R.id.txtStartDate))
            val actEndDate = parseDatePicker(findViewById<DatePicker>(R.id.txtEndDate))

            if (actCategory != null) {
                val newActivity = ToolBox.ActivityDataClass(
                    actTitle,
                    actClient,
                    actLocation,
                    actCategory,
                    actDuration,
                    actStartDate,
                    actEndDate
                )

                ToolBox.ActivityManager.addActivity(newActivity)
            }
        }
    }

    fun parseDatePicker(datePicker: DatePicker): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month + 1 // Month starts from 0
        val year = datePicker.year
        return "$year-$month-$day"
    }

    fun getCategoryByName(categoryName: String): ToolBox.CategoryDataClass? {
        val categoryList = ToolBox.CategoryManager.getCategoryList()
        return categoryList.find { it.name == categoryName }
    }
}