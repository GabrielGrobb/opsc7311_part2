package com.example.opsc7311_part2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.opsc7311_part2.R.id.datePickerButton
import com.example.opsc7311_part2.databinding.ActivityAddCategoryBinding
import com.example.opsc7311_part2.databinding.ActivityCaptureTaskBinding

class AddCategory : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }
    }




}