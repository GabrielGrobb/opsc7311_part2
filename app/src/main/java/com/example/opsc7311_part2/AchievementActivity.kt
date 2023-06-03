package com.example.opsc7311_part2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView

class AchievementActivity : Activity() {

    private lateinit var achievementsProgressBar: ProgressBar
    private lateinit var achievementsProgressText: TextView
    private lateinit var task1ProgressBar: ProgressBar
    private lateinit var task1ProgressText: TextView
    private lateinit var task2ProgressBar: ProgressBar
    private lateinit var task2ProgressText: TextView
    private lateinit var task3ProgressBar: ProgressBar
    private lateinit var task3ProgressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)

        achievementsProgressBar = findViewById(R.id.AchievementsProgressBar)
        achievementsProgressText = findViewById(R.id.achievementsProgressText)
        task1ProgressBar = findViewById(R.id.Task1ProgressBar)
        task1ProgressText = findViewById(R.id.Task1ProgressText)
        task2ProgressBar = findViewById(R.id.Task2ProgressBar)
        task2ProgressText = findViewById(R.id.Task2ProgressText)
        task3ProgressBar = findViewById(R.id.Task3ProgressBar)
        task3ProgressText = findViewById(R.id.Task3ProgressText)

        //Initial progress values
        achievementsProgressBar.progress = 0
        achievementsProgressText.text = "0%"
        task1ProgressBar.progress = 0
        task1ProgressText.text = "0%"
        task2ProgressBar.progress = 0
        task2ProgressText.text = "0%"
        task3ProgressBar.progress = 0
        task3ProgressText.text = "0%"
    }
}
