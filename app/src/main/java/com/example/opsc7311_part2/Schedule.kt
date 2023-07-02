package com.example.opsc7311_part2

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityScheduleBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale


class Schedule : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var menuButton: ImageView
    private lateinit var schedulePageTitle: TextView
    private lateinit var currentDate: TextView
    private lateinit var completedTasks: TextView
    private lateinit var spinnerSchedule: Spinner
    private lateinit var dateToday: TextView
    private lateinit var dateTomorrow: TextView
    private lateinit var layoutSchedule: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Finding the TextView we want to update
        var CurrentDateTextView = findViewById<TextView>(R.id.CurrentDate)
        //Setting the value of the text view to the Calendar's current date
        CurrentDateTextView.text = ToolBox.CategoryManager.getCurrentDateString()


        setSupportActionBar(binding.navToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var toggleOnOff = ActionBarDrawerToggle(
            this,
            binding.drawerLayout, binding.navToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggleOnOff)
        toggleOnOff.syncState()

        binding.navView.bringToFront()
        binding.navView.setNavigationItemSelectedListener(this)

        currentDate = findViewById(R.id.CurrentDate)
        completedTasks = findViewById(R.id.txtCompletedTasks)


        val activityList = ToolBox.DBManager.getActivitiesFromDB()

        val displayView = findViewById<LinearLayout>(R.id.layout)

        for (activity in activityList) {
            if (activity.startDate >= CurrentDateTextView.text.toString()) {
                // Match found, perform desired actions with the activity


                val customView = custom_activity_icon(this)

                customView.setActID(activity.actID)
                customView.setActName(activity.title)

                /*activity.actImage?.let { bitmap ->
                    customView.setIcon(bitmap)
                }*/

                displayView.addView(customView)
            }
        }
        val tilEndDate: TextInputLayout = findViewById(R.id.til_EndDate)
        val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)

        tilEndDate.setEndIconOnClickListener() {
            showDatePickerDialog(txtEndDate)
        }
    }

    //............................................................................................//

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(applicationContext, HomePageTest::class.java)
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

    override fun onBackPressed() {
        //if the drawer is open, close it
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            //otherwise, let the super class handle it
            super.onBackPressed()
        }
    }

    //............................................................................................//

    override fun onClick(v: View?) {
        /*TODO("Not yet implemented")*/
    }

    //............................................................................................//
    private fun showDatePickerDialog(textField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val txtEndDate: TextInputEditText = findViewById(R.id.txtEndDate)
        val displayView = findViewById<LinearLayout>(R.id.layout)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the text field with the selected date
                val formattedDate =
                    String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                textField.setText(formattedDate)
            },
            year,
            month,
            day
        )
        //getting activity list
        val activityList = ToolBox.DBManager.getActivitiesFromDB()
        // Set a custom date range
        val startDate =
            ToolBox.CategoryManager.getCurrentDateString()// Set your start date as a Calendar instance
        // Set a listener to disable specific dates
        datePickerDialog.datePicker.init(year, month, day) { datePicker, year, month, day ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val endDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val endDateMillis =
                endDate.timeInMillis // Convert startDate and endDate to Date objects
            val startDateMillis =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDate)?.time ?: 0
            // Convert startDateMillis and endDateMillis to Calendar instances
            val startCalendar = Calendar.getInstance().apply {
                timeInMillis = startDateMillis
            }
            val endCalendar = Calendar.getInstance().apply {
                timeInMillis = endDateMillis
            }
            // Disable dates outside the specified range
            if (selectedDate.before(startCalendar) || selectedDate.after(endCalendar)) {
                // Disable the date by setting it to an invalid minimum date
                datePicker.minDate = startCalendar.timeInMillis
                // Set selected date to start date if it's before the start date or after the end date
                if (selectedDate.before(startCalendar)) {
                    selectedDate.timeInMillis = startCalendar.timeInMillis
                } else if (selectedDate.after(endCalendar)) {
                    selectedDate.timeInMillis = endCalendar.timeInMillis
                }
                // Formatting date
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDateString = sdf.format(selectedDate.time)
                txtEndDate.setText(selectedDateString)
            }
            displayView.removeAllViews()
            for (activity in activityList) {
                if (activity.endDate <= txtEndDate.text.toString()) {
                    val customView = custom_activity_icon(this)
                    customView.setActID(activity.actID)
                    customView.setActName(activity.title)
                    // Set the bitmap image
                    //customView.setIcon(imageResource)
                    /*activity.actImage?.let { bitmap ->
                        customView.setIcon(bitmap)
                    }*/
                    // customView.setIcon(imageResource)
                    displayView.addView(customView)
                }
            }
        }
        datePickerDialog.show()
    }
    //............................................................................................//
}
