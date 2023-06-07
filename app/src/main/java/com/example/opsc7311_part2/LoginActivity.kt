package com.example.opsc7311_part2

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale.Category

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSubmit = findViewById<Button>(R.id.btnLogin)

        val registerText = findViewById<TextView>(R.id.registerText)
        registerText.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        btnSubmit.setOnClickListener(){
            //If anything breaks in the code within the try method the error message will show
            try {
                val editText = findViewById<TextInputEditText>(R.id.txtUsername)
                val name = editText.text.toString()

                val passText = findViewById<TextInputEditText>(R.id.txtPassword)
                val pas = passText.text.toString()

                //Getting the stored values
                val currentSettings = ToolBox.AccountManager.getSettingsObject()
                val storedUsername = currentSettings.username
                val storedPassword = currentSettings.password

                if (name == storedUsername && pas == storedPassword) {
                    val intent = Intent(this, HomePageTest::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Toast.makeText(this, "An error has occurred within the code $:{e.message})", Toast.LENGTH_LONG).show()
            }
        }
    }
}

//.........................................EndOfFile..............................................//