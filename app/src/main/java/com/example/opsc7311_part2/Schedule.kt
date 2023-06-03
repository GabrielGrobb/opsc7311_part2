package com.example.opsc7311_part2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView

class Schedule : AppCompatActivity() {
    private lateinit var menuButton: ImageView
    private lateinit var schedulePageTitle: TextView
    private lateinit var currentDate: TextView
    private lateinit var completedTasks: TextView
    private lateinit var spinnerSchedule: Spinner
    private lateinit var dateToday: TextView
    private lateinit var dateTomorrow: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        menuButton = findViewById(R.id.IV_menubutton)
        schedulePageTitle = findViewById(R.id.txt_SchedulePageTitle)
        currentDate = findViewById(R.id.CurrentDate)
        completedTasks = findViewById(R.id.txtCompletedTasks)
        spinnerSchedule = findViewById(R.id.spinnerSchedule)
        dateToday = findViewById(R.id.DateToday)
        dateTomorrow = findViewById(R.id.DateTomorrow)

        // Set click listener for the menu button
        menuButton.setOnClickListener {
            // Handle menu button click
        }

        // TODO: Add code for handling other UI elements and implementing functionality

    }
}