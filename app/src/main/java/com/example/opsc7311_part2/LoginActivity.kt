package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.opsc7311_part2.databinding.ActivityCaptureTaskBinding
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale.Category

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSubmit = findViewById<Button>(R.id.btnLogin)

        btnSubmit.setOnClickListener(){
            val editText = findViewById<TextInputEditText>(R.id.txtUsername)
            val name = editText.text.toString()

            val passText = findViewById<TextInputEditText>(R.id.txtPassword)
            val pas = passText.text.toString()


            // Creating of the User() class.
            var user = User()

            // calling the method getUser() within User().
            var myMap = user.getUser()

            // A boolean to check if the user exists.
            var bool = logUser(myMap, name, pas)

            if(bool)
            {
                // (intent) Allows you to navigate from one activity to another.
                val intent = Intent(this,AddActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"username and password is wrong", Toast.LENGTH_LONG).show()

            }
        }
    }

    fun logUser(myMap:Map<String, String>, name: String, pas:String):Boolean
    {
        for (keys in myMap)
        {
            if (keys.key == name && keys.value == pas)
            {
                return true
            }
        }
        return false
    }
}