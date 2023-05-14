package com.example.opsc7311_part2

//Add necessary references for view
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activity)

        //Get a reference to the view that we want to access
        //
        //Start Date for both text box and editable text
        val  tilStartDate: TextInputLayout = findViewById(R.id.til_StartDate)
        val txtStartDate: TextInputEditText = findViewById(R.id.txtStartDate)



        //Manipulate the view
        //Adding event handlers to check when the end icon is clicked
        fun performActionOnClick() {

        }

        tilStartDate.setEndIconOnClickListener {
            // Code to be executed when the end icon is clicked
            performActionOnClick()
        }

    }
}