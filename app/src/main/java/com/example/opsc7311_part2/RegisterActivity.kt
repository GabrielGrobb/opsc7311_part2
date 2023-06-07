package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var currentSettings = ToolBox.AccountManager.getSettingsObject()
        var submitButton = findViewById<Button>(R.id.btnSubmit)

        var email = findViewById<TextInputEditText>(R.id.txtEmail).text
        var username = findViewById<TextInputEditText>(R.id.txtUsername).text
        var firstname = findViewById<TextInputEditText>(R.id.txtFirstname).text
        var surname = findViewById<TextInputEditText>(R.id.txtSurname).text
        var password = findViewById<TextInputEditText>(R.id.txtPassword).text
        var confirmPassword = findViewById<TextInputEditText>(R.id.txtConfirmPassword).text

        submitButton.setOnClickListener{
        if(password?.toString().equals(confirmPassword.toString())){
            currentSettings.updateSettings(
                "default",
                1,
                1,
                email.toString(),
                username.toString(),
                firstname.toString(),
                surname.toString(),
                password.toString()
            )
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            }
            else{
                val toast = Toast.makeText(this, "Password and Confirm Password Mismatch", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    }
}
//.........................................EndOfFile..............................................//
