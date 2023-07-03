package com.example.opsc7311_part2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var currentSettings = ToolBox.AccountManager.getSettingsObject()
        var submitButton = findViewById<Button>(R.id.btnSubmit)

        // Submit button click listener
        submitButton.setOnClickListener {
            var emailInput = findViewById<TextInputEditText>(R.id.txtEmail)
            var usernameInput = findViewById<TextInputEditText>(R.id.txtUsername)
            var firstnameInput = findViewById<TextInputEditText>(R.id.txtFirstname)
            var surnameInput = findViewById<TextInputEditText>(R.id.txtSurname)
            var passwordInput = findViewById<TextInputEditText>(R.id.txtPassword)
            var confirmPasswordInput = findViewById<TextInputEditText>(R.id.txtConfirmPassword)
            val userImage = ContextCompat.getDrawable(
                this,
                R.drawable.default_profile
            ) // Replace with your drawable resource ID
            // Clear any previous error state and tooltips
            clearErrorState(
                emailInput,
                usernameInput,
                firstnameInput,
                surnameInput,
                passwordInput,
                confirmPasswordInput
            )
            // Get the input values as strings
            val email = emailInput.text.toString()
            val username = usernameInput.text.toString()
            val firstname = firstnameInput.text.toString()
            val surname = surnameInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()
            val bitmap: Bitmap? = (userImage as? BitmapDrawable)?.bitmap

            // Perform validations
            val validation = Validation()
            val invalidFields = mutableListOf<String>()
            //validating email
            if (!validation.isValidEmail(email)) {
                invalidFields.add("Email")
                emailInput.error = "Must be a valid email address!"
            }
            //validating username
            if (!validation.validateStringsWithNumbers(username)) {
                invalidFields.add("Username")
                usernameInput.error = "Must be numbers and letters!"
            }
            //validating firstname
            if (!validation.validateStringsNoNumbers(firstname)) {
                invalidFields.add("Firstname")
                firstnameInput.error = "Must be letters only!"
            }

            //validating surname
            if (!validation.validateStringsNoNumbers(surname)) {
                invalidFields.add("Surname")
                surnameInput.error = "Must be letters only!"
            }
            //ensuring passwords match
            if (password != confirmPassword) {
                invalidFields.add("confirmPassword")
                confirmPasswordInput.error = "must match password!"
            }
            //validating password
            if (password.length < 10) {
                invalidFields.add("Password")
                passwordInput.error = "Must be longer than 10 chars!"
            }
            //checking for errors and displaying toast message if any are found
            if (invalidFields.isNotEmpty()) {
                val errorMessage = "Invalid input/s. Please check the following field(s): ${
                    invalidFields.joinToString(", ")
                }"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop execution if any field is invalid
            }
            // All validations passed, proceed with registration
            currentSettings.updateSettings(
                bitmap,
                1,
                1,
                email,
                username,
                firstname,
                surname,
                password
            )
            ToolBox.AccountManager.persistUser(currentSettings)
            ToolBox.AccountManager.currentSettings = currentSettings
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    //........................................................................................//

    private fun clearErrorState(vararg textInputs: TextInputEditText) {
        for (textInput in textInputs) {
            textInput.error = null
        }
    }
}
//----------------------------------------END-OF-FILE------------------------------------------