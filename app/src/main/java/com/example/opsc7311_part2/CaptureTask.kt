package com.example.opsc7311_part2

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.example.opsc7311_part2.ToolBox.DBManager.getDocumentIDByTypeID
import com.example.opsc7311_part2.ToolBox.DBManager.updateActivityCurrentTime
import com.example.opsc7311_part2.ToolBox.DBManager.updateActivitySavedTimeSpent
import com.example.opsc7311_part2.databinding.ActivityCaptureTaskBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.time.Duration
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
    private val maxProgress = 100
    private var maxTime: Duration = Duration.ZERO
    private var timerStarted = false


    //............................................................................................//

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptureTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var currentProgress = 0//currentActivity.currentTimeSpent

        ///---------------------------------------------------------------------------------------//


        //Setting the Current Date
        var CurrentDateTextView = findViewById<TextView>(R.id.CurrentDate)
        CurrentDateTextView.text = ToolBox.CategoryManager.getCurrentDateString()

        val actName = intent.getStringExtra("activityName")
        val actID = intent.getIntExtra("activityID", -1)

        val bitmap = intent.getParcelableExtra<Bitmap>("imageIcon")
        val activityImage = findViewById<ImageView>(R.id.imageView)
        activityImage.setImageBitmap(bitmap)

        val activityNameTextView = findViewById<TextView>(R.id.txtActivityName)

        activityNameTextView.text = actName

        //Getting the current Activity object we are working with
        val activityObject = ToolBox.ActivityManager.getActivityObjectByID(actID)

        ///---------------------------------------------------------------------------------------//
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

        ///---------------------------------------------------------------------------------------//


        timer = Timer()

        progressBar = findViewById(R.id.progressBar)

        val btnRecord = findViewById<ImageButton>(R.id.btnStop)
        btnRecord.setOnClickListener{
            lifecycleScope.launch{
                recordTimerToActivity()
            }
            println("111111")
        }

        val maxTimeTextView = findViewById<TextView>(R.id.txtDuration)
        maxTimeTextView.text = activityObject.duration.toMinutes().toString()

        val maxTimeHours = maxTimeTextView.text.toString().toDouble() // Convert the input to a Double or use a default value if conversion fails
        maxTime = Duration.ofHours(maxTimeHours.toLong()) // Create a Duration object using the converted hours

        //Setting MaxTime view
        maxTimeTextView.text = "Max Time: "+activityObject.duration.toHours()

        ///---------------------------------------------------------------------------------------//


        //Date formatting spinner stuff
        val spinner: Spinner = findViewById(R.id.dropDownTimeFormat)

        //Handling logic for changing how the time is displayed
        val displayDuration = activityObject.duration
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position)
                when(selectedItem){
                    "Hrs" ->{
                        val hours = displayDuration.toHours()
                        val timeString = java.lang.StringBuilder()
                        timeString.append("Max Time: " + hours + " hour")
                        if(hours>1){timeString.append("s")}
                        maxTimeTextView.text = timeString
                    }
                    "Min" ->{
                        val minutes = displayDuration.toMinutes()
                        val timeString = "Max Time: " + minutes + " minutes"
                        maxTimeTextView.text = timeString
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }



        ///---------------------------------------------------------------------------------------//

        val activityList = ToolBox.DBManager.getActivitiesFromDB()

        for (activity in activityList) {
            if (activity.actID == actID && activity.title == actName) {
                //val currentActivityId = activity.actID

                    val timerValue = formatTime(
                        activity.currentTimeSpent.seconds.toInt() % 60,
                        activity.currentTimeSpent.toMinutes().toInt() % 60,
                        activity.currentTimeSpent.toHours().toInt()
                    )

                    val timerTextView = findViewById<TextView>(R.id.txtTimerSaved)
                    timerTextView.text = timerValue

                if(activity.currentTimeSpent >= activity.duration)
                {
                    Toast.makeText(this,"Activity has been completed", Toast.LENGTH_LONG).show()
                    stopStartButton.isEnabled = false
                }

            }
        }
    }

    //............................................................................................//

    //Updates the given activity in the DB
    private suspend fun recordTimerToActivity() {
        /*// Get the current activity
        val currentActivity = ToolBox.ActivityManager.getActivityObjectByID(intent.getIntExtra("activityID", -1))

        // Capture the current time as a string in the format "HH:mm:ss"
        val capturedTime = getTimerText()

        // Remove leading and trailing whitespace from the captured time string
        val trimmedTime = capturedTime.trim()

        // Split the trimmed time string into hours, minutes, and seconds
        val timeParts = trimmedTime.split(" : ")
        if (timeParts.size == 3) {
            val hours = timeParts[0].toIntOrNull()
            val minutes = timeParts[1].toIntOrNull()
            val seconds = timeParts[2].toIntOrNull()

            if (hours != null && minutes != null && seconds != null) {
                // Convert the time parts to a Duration object
                val updatedTimeSpent = Duration.ofSeconds(seconds.toLong())
                    .plusMinutes(minutes.toLong())
                    .plusHours(hours.toLong())

                // Get the difference between the current and saved time
                val timeDifference = updatedTimeSpent + currentActivity.savedTimeSpent

                // Update the current activity's currentTimeSpent with the time difference
                currentActivity.currentTimeSpent = timeDifference
                updateActivityCurrentTime(getDocumentIDByTypeID("Activities", "actID", currentActivity.actID), timeDifference)

                // Update the savedTimeSpent with the current updatedTimeSpent
                currentActivity.savedTimeSpent = timeDifference
                updateActivitySavedTimeSpent(getDocumentIDByTypeID("Activities", "actID", currentActivity.actID), timeDifference)

                // Get the category of the current activity
                val category = ToolBox.CategoryManager.getCategoryByID(currentActivity.categoryId)

                // Calculate the total activity time spent in the category
                val totalActivityTimeSpent = ToolBox.CategoryManager.getActivitiesForCategory(category.catID.toString()).sumOf { it.currentTimeSpent.toMillis() }

                // Update the activityTimeSpent in the CategoryDataClass
                category.activityTimeSpent = Duration.ofMillis(totalActivityTimeSpent)



                // ... Perform any additional actions or save the updated activity and category objects as needed

                // Finish the current activity and return to the previous activity
                finish()
            }*/
        // Get the current activity
        val currentActivity = ToolBox.ActivityManager.getActivityObjectByID(intent.getIntExtra("activityID", -1))

        // Capture the current time as a string in the format "HH:mm:ss"
        val capturedTime = getTimerText()

        val documentId = getDocumentIDByTypeID("Activities", "actID", currentActivity.actID)

        //val documentIdCategory = ToolBox.CategoryManager.getCategoryDocumentIDByTypeID("Category", "catID", currentActivity.categoryId)

        // Remove leading and trailing whitespace from the captured time string
        val trimmedTime = capturedTime.trim()

        // Split the trimmed time string into hours, minutes, and seconds
        val timeParts = trimmedTime.split(" : ")

        if (timeParts.size == 3) {
            val hours = timeParts[0].toIntOrNull()
            val minutes = timeParts[1].toIntOrNull()
            val seconds = timeParts[2].toIntOrNull()

            if (hours != null && minutes != null && seconds != null) {
                // Convert the time parts to a Duration object
                val updatedTimeSpent = Duration.ofSeconds(seconds.toLong())
                    .plusMinutes(minutes.toLong())
                    .plusHours(hours.toLong())

                // Get the difference between the current and saved time
                val timeDifference = updatedTimeSpent + currentActivity.savedTimeSpent

                // Update the current activity's currentTimeSpent with the time difference
                currentActivity.currentTimeSpent = timeDifference
                updateActivityCurrentTime(documentId, timeDifference)

                // Update the savedTimeSpent with the current updatedTimeSpent
                currentActivity.savedTimeSpent = timeDifference
                updateActivitySavedTimeSpent(documentId, timeDifference)

                // Get the category of the current activity
                val category = ToolBox.CategoryManager.getCategoryByID(currentActivity.categoryId)

                // Calculate the total activity time spent in the category
                val categoryId = category.catID.toString()
                val categoryDocumentId = ToolBox.CategoryManager.getCategoryDocumentIDByTypeID("Category", "catID", categoryId) // Replace with the actual method to get the category document ID

                if (categoryDocumentId != null) {
                    val totalActivityTimeSpent = ToolBox.CategoryManager.calcCategoryTime(categoryId)

                    // Update the activityTimeSpent in the CategoryDataClass
                    ToolBox.CategoryManager.updateCategoryTimeSpent(categoryDocumentId, totalActivityTimeSpent)

                    // Finish the current activity and return to the previous activity
                    finish()
                } else {
                    // Handle the case where the category document ID is null
                    Toast.makeText(this, "Failed to get category document ID", Toast.LENGTH_SHORT).show()
                }

                /*// Calculate the total activity time spent in the category
                //val totalActivityTimeSpent = ToolBox.CategoryManager.getActivitiesForCategory(category.catID.toString()).sumOf { it.currentTimeSpent.toMillis() }
                val totalActivityTimeSpent = ToolBox.CategoryManager.calcCategoryTime(category.catID.toString())

                // Update the activityTimeSpent in the CategoryDataClass
                ToolBox.CategoryManager.updateCategoryTimeSpent(documentIdCategory, totalActivityTimeSpent)

                //category.activityTimeSpent = totalActivityTimeSpent

                //category.activityTimeSpent = Duration.ofMillis(totalActivityTimeSpent)

                // Finish the current activity and return to the previous activity
                finish()*/
            }
        } else {
            // Error handling if the captured time string is not in the expected format
            Toast.makeText(this, "Invalid time format: $capturedTime", Toast.LENGTH_SHORT).show()
        }
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

        // Initialize the timer
        timer = Timer()

        // Sped up the timer counter to see if the progress bar progresses.
        // remember to change period: 1000
        timer.scheduleAtFixedRate(timerTask, 0, 10)
    }

    //............................................................................................//

    private fun updateProgressBar() {
        val maxTimeInMinutes = maxTime.toHours().toInt()
        val currentProgress = (((time/60) / maxTimeInMinutes) * maxProgress).toInt()
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
        return String.format("%02d", hours) + " : " +
                String.format("%02d", minutes) + " : " +
                String.format("%02d", seconds)
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

            R.id.nav_home -> {
                val intent = Intent(applicationContext, HomePageTest::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.nav_schedule -> {
                val intent = Intent(applicationContext, Schedule::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.nav_achievements -> {
                val intent = Intent(applicationContext, AchievementsPage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            R.id.nav_graph -> {
                val intent = Intent(applicationContext, Graph::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }

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