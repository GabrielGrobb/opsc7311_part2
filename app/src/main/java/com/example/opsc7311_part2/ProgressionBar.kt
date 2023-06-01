package com.example.opsc7311_part2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import android.widget.TextView



class ProgressionBar : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        progressBar = findViewById(R.id.AchievementsProgressBar)
        progressText = findViewById(R.id.achievementsProgressText)

        updateProgress(50) // Set initial progress value

        // Call this method to update the progress dynamically
        // You can call it from any event or process in your code
        // and update the progress and text accordingly
        // For example: updateProgress(75)
    }

    private fun updateProgress(progress: Int) {
        progressBar.progress = progress
        progressText.text = "$progress%"

        // You can also format the progress text as desired
        // For example: progressText.text = getString(R.string.progress_format, progress)
    }
}