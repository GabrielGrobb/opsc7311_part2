package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityCaptureTaskBinding
import com.google.android.material.navigation.NavigationView
import java.util.*

class CaptureTask : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityCaptureTaskBinding

    private lateinit var timerText: TextView
    private lateinit var stopStartButton: ImageButton
    private lateinit var timer: Timer
    private lateinit var timerTask: TimerTask
    private var time = 0.0
    private lateinit var progressBar: ProgressBar
    private lateinit var progressionBar: View
    private lateinit var maxTimeTextView: TextView
    private val maxProgress = 100
    private var currentProgress = 0
    private var maxTime = 240.0
    private var timerStarted = false
    //............................................................................................//

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.navToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var toggleOnOff = ActionBarDrawerToggle(this,
            binding.drawerLayout, binding.navToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        binding.drawerLayout.addDrawerListener(toggleOnOff)
        toggleOnOff.syncState()

        binding.navView.bringToFront()
        binding.navView.setNavigationItemSelectedListener(this)

        populateDropDowns()

        timerText = findViewById(R.id.txtTimerCounter)
        stopStartButton = findViewById(R.id.btnPlay)
        stopStartButton.setOnClickListener{startStopTapped()}

        timer = Timer()

        progressBar = findViewById(R.id.progressBar)

        maxTimeTextView = findViewById(R.id.txtDuration)
        maxTimeTextView.text = getString(R.string.max_time, maxTime)

    }

    //............................................................................................//

    private fun startStopTapped()
    {
        if (!timerStarted)
        {
            timerStarted = true
            setButtonUI(R.drawable.pause_circle)
            startTimer()
        }
        else
        {
            timerStarted = false
            setButtonUI(R.drawable.baseline_play_circle_24)
            timerTask.cancel()
        }
    }

    //............................................................................................//

    private fun setButtonUI(drawableResId: Int) {
        stopStartButton.setImageResource(drawableResId)
    }

    //............................................................................................//

    private fun startTimer()
    {
        timerTask = object : TimerTask()
        {
            override fun run()
            {
                runOnUiThread {
                    time++
                    timerText.text = getTimerText()
                    updateProgressBar()
                }
            }
        }

        // Sped up the timer counter to see if the progress bar progresses.
        // remember to change period: 1000
        timer.scheduleAtFixedRate(timerTask, 0, 10)
    }

    //............................................................................................//

    private fun updateProgressBar() {
        currentProgress = (((time/60) / maxTime) * maxProgress).toInt()
        progressBar.progress = currentProgress
    }

    private fun getTimerText(): String {
        val rounded = time.toInt()

        val seconds = ((rounded % 86400) % 3600) % 60
        val minutes = ((rounded % 86400) % 3600) / 60
        val hours = (rounded % 86400) / 3600

        return formatTime(seconds, minutes, hours)
    }

    //............................................................................................//

    private fun formatTime(seconds: Int, minutes: Int, hours: Int): String {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds)
    }

    //............................................................................................//

    private fun populateDropDowns()
    {
        val spinner: Spinner = findViewById(R.id.dropDownTimeFormat)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.timeformat_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

    }

    //............................................................................................//

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {

        when(item.itemId) {

            R.id.nav_account -> {
                val intent = Intent(applicationContext, AccountSettings::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        // return true marks the item as selected
        return true
    }

    //............................................................................................//

    override fun onBackPressed()
    {
        //if the drawer is open, close it
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
            //otherwise, let the super class handle it
            super.onBackPressed()
        }
    }

    //............................................................................................//

    override fun onClick(v: View?)
    {
        /*TODO("Not yet implemented")*/
    }

    //............................................................................................//
}
//.........................................EndOfFile..............................................//