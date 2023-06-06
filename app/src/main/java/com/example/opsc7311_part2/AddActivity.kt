package com.example.opsc7311_part2

//Add necessary references for view
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.opsc7311_part2.databinding.ActivityAddActivityBinding
import com.example.opsc7311_part2.databinding.ActivityAddCategoryBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import android.app.DatePickerDialog
import android.content.Intent
import android.text.AutoText
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Spinner
import java.util.Date

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddActivityBinding

    private var activityCounter = ToolBox.ActivityManager.getActivityList().size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        //Adding an Activity


        //Views
       // val txtTitle: TextInputEditText = findViewById(R.id.txtTitle)
        //val txtClient: TextInputEditText = findViewById(R.id.txtClient)

        val tilLocation: TextInputLayout = findViewById(R.id.til_Location)
        //val txtLocation: TextInputEditText = findViewById(R.id.txtLocation)

        val tilCategory: TextInputLayout = findViewById(R.id.til_Category)
        val txtCategory = findViewById<Spinner>(R.id.spCategory)
       // val categoryAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getCategoryNames())
        //txtCategory.setAdapter(categoryAdapter)

        val tilStartDate: TextInputLayout = findViewById(R.id.til_StartDate)
        //val txtStartDate: TextInputEditText = findViewById(R.id.txtStartDate)

        val tilEndDate: TextInputLayout = findViewById(R.id.til_EndDate)
        //val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)
        val imgActivityIcon: ImageView = findViewById(R.id.ActivityIcon)


        val txtTitle: TextInputEditText = findViewById(R.id.txtTitle)
        val txtClient: TextInputEditText = findViewById(R.id.txtClient)
        val txtLocation: TextInputEditText = findViewById(R.id.txtLocation)
        val spCategory: Spinner = findViewById(R.id.spCategory)
        val txtDuration: TextInputEditText = findViewById(R.id.txtDuration)
        val txtStartDate: TextInputEditText = findViewById(R.id.txtStartDate)
        val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)

        val categoryAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getCategoryNames())
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = categoryAdapter

        val categoryList = ToolBox.CategoryManager.getCategoryList()

        binding.btnAddActivity.setOnClickListener {

            // Increment the categoryCounter
            activityCounter++

            /*val actTitle = txtTitle.text.toString()
            val actClient = txtClient.text.toString()
            val actLocation = txtLocation.text.toString()
            val actCategoryName = txtCategory.text.toString()
            val actDuration = 0 // Replace with the actual duration value
            val actStartDate = showDatePickerDialog(R.id.txtStartDate)
            //val actEndDate = "" // Replace with the actual end date value*/
            val actStartDate = findViewById<TextInputEditText>(R.id.txtStartDate).text
            val selectedCategory = spCategory.selectedItem.toString()

            val category = categoryList.find { it.name == selectedCategory }
            val categoryId = category?.catID ?: -1 // Default value if category not found

                val newActivity = ToolBox.ActivityDataClass(
                    activityCounter,
                    txtTitle.text.toString(),
                    txtClient.text.toString(),
                    txtLocation.text.toString(),
                    selectedCategory,
                    categoryId,
                    txtDuration.text.toString().toInt(),
                    actStartDate.toString(),
                    //actEndDate
                )
                //ToolBox.ActivityManager.addActivity(newActivity)
            ToolBox.ActivityManager.addActivity(newActivity)
            category?.activities?.add(newActivity)
            /*val category = categoryList.find { it.name == selectedCategory }
            category?.activities?.add(newActivity)*/

                // Return to the HomePage
                val intent = Intent(this, HomePageTest::class.java)
                startActivity(intent)
        }

        //Functions
        //Allows the user to select an icon for a given activity from the list of icons
        fun performActionOnClick() {

        }





        //

        //Listeners
        tilLocation.setEndIconOnClickListener(){

        }

        tilCategory.setEndIconOnClickListener(){

        }

        tilStartDate.setEndIconOnClickListener {
            showDatePickerDialog(txtStartDate)
        }

        tilEndDate.setEndIconOnClickListener(){
            showDatePickerDialog(txtEndDate)
        }

    }

    private fun getCategoryNames(): List<String> {
        val categoryList = ToolBox.CategoryManager.getCategoryList()
        return categoryList.map { it.name }
    }

    private fun showDatePickerDialog(textField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the text field with the selected date
                val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                textField.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}