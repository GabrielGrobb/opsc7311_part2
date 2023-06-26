package com.example.opsc7311_part2

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import android.widget.TextView



class ProgressionBar : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        *//* progressBar.max = 100
        val currentProgress =
        ObjectAnimator.ofInt(AchievementsProgressBar, "") *//*

        progressBar = findViewById(R.id.AchievementsProgressBar)
        progressText = findViewById(R.id.achievementsProgressText)

        updateProgress(50) // Set initial progress value


    }

    private fun updateProgress(progress: Int) {
        progressBar.progress = progress
        progressText.text = "$progress%"


    }*/
}