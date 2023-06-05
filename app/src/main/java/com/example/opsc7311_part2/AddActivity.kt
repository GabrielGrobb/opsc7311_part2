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
import android.widget.EditText

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        //Adding an Activity


        //Views
        val tilLocation: TextInputLayout = findViewById(R.id.til_Location)
        val txtLocation: TextInputEditText = findViewById(R.id.txtLocation)
        val tilCategory: TextInputLayout = findViewById(R.id.til_Category)
        val txtCategory: TextInputEditText = findViewById(R.id.txtCategory)
        val tilStartDate: TextInputLayout = findViewById(R.id.til_StartDate)
        val txtStartDate: TextInputEditText = findViewById(R.id.txtStartDate)
        val tilEndDate: TextInputLayout = findViewById(R.id.til_EndDate)
        val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)
        val imgActivityIcon: ImageView = findViewById(R.id.ActivityIcon)


        //Functions
        //Allows the user to select an icon for a given activity from the list of icons
        fun performActionOnClick() {

        }

        //Shows the date picker dialog for a given text input
        fun showDatePickerDialog(textField: EditText) {
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
}