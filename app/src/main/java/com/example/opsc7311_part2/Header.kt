package com.example.opsc7311_part2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

//-----------------------------------------------------------------------------------//

//To display current logged in Username on the Header

class Header : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header)

        // Retrieve the username from ToolBox
        val username = ToolBox.AccountManager.getSettingsObject().username


        // Set the username to the TextView or show the hint as a placeholder
        val txtUsername = findViewById<TextView>(R.id.txtUsername)

        txtUsername.text = username
        if (username.isNullOrEmpty()) {
            txtUsername.text = null
            txtUsername.hint = "Username"
        } else {
            txtUsername.text = username
            txtUsername.hint = null
        }
    }
}




//-----------------------------------------------------------------------------------//