package com.example.opsc7311_part2

//Add necessary references for view
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activity)

        //Views
        val backButton: ImageView = findViewById(R.id.til_BackButton)
        val tilLocation: TextInputLayout = findViewById(R.id.til_Location)
        val txtLocation: TextInputEditText = findViewById(R.id.txtLocation)
        val tilCategory: TextInputLayout = findViewById(R.id.til_Category)
        val txtCategory: TextInputEditText = findViewById(R.id.txtCategory)
        val tilStartDate: TextInputLayout = findViewById(R.id.til_StartDate)
        val txtStartDate: TextInputEditText = findViewById(R.id.txtStartDate)
        val tilEndDate: TextInputLayout = findViewById(R.id.til_EndDate)
        val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)


        //Functions
        fun performActionOnClick() {

        }

        //Listeners
        backButton.setOnClickListener(){

        }

        tilLocation.setEndIconOnClickListener(){

        }

        tilCategory.setEndIconOnClickListener(){

        }

        tilStartDate.setEndIconOnClickListener {
            // Code to be executed when the end icon is clicked
            performActionOnClick()
        }

        tilEndDate.setEndIconOnClickListener(){

        }

    }
}