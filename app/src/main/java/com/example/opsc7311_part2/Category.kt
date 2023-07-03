package com.example.opsc7311_part2

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityCategoryBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.HashSet

class Category : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //----------------------------------------------------------------------------------------//

        /// This is for the navigation burger menu
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

        //----------------------------------------------------------------------------------------//

        val catID = intent.getIntExtra("categoryID", 1) // Default value if category ID is not provided

        val catName = intent.getStringExtra("categoryName")
        val imgResource = intent.getIntExtra("imageIcon", 0)

        val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)
        //val categoryIconImageView = findViewById<ImageView>(R.id.iconPicture)
        val categoryIdentification = findViewById<TextView>(R.id.categoryId)

        categoryNameTextView.text = catName
        //categoryIconImageView.setImageResource(imgResource)
        categoryIdentification.text = catID.toString()

        val categoryList = ToolBox.CategoryManager.getCategoryList()
        val displayView = findViewById<LinearLayout>(R.id.ActivityView)
        //finding end date view
        val tilEndDate = findViewById<TextInputLayout>(R.id.til_EndDate)
        //finding start date view
        var tilStartDate = findViewById<TextInputLayout>(R.id.til_StartDate)
        //finding textbox for end date
        var txtEndDate = findViewById<TextInputEditText>(R.id.txtEndDate)
        //finding textbox for start date
        var txtStartDate = findViewById<TextInputEditText>(R.id.txtStartDate)
        //finding btn view to filter activities
        var btnFilterActivities = findViewById<Button>(R.id.btnFilterActivities)
        /// Creating a HashSet for the activities.
        val addedActivities = HashSet<Int>()

        //Loop through all categories
        for (category in categoryList)
        {
            //Create an activity list for the category
            val activities = ToolBox.CategoryManager.getActivitiesForCategory(catID.toString())

            //Loop through the activity list for said category
            for (activity in activities)
            {
                val currentActivityId = activity.actID

                /// Preventing duplication of an activity in the linearlayout
                if (!addedActivities.contains(currentActivityId))
                {
                    val totalCategoryHours = findViewById<TextView>(R.id.totalCategoryHours)

                    val categoryTime = ToolBox.CategoryManager.calcCategoryTime(catID.toString())

                    val hours = categoryTime.toHours()
                    val minutes = categoryTime.toMinutes() % 60
                    val seconds = categoryTime.seconds % 60

                    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    totalCategoryHours.text = formattedTime

                    /// val imageResource = resources.getIdentifier("home_icon", "drawable", packageName)
                    val customView = custom_activity_icon(this)

                    /// Set activity ID and name
                    customView.setActID(activity.actID)
                    customView.setActName(activity.title)


                    /// Set the bitmap image
                    activity.actImage?.let {bitmap -> customView.setIcon(bitmap)}

                    /// Adding the views to the linearlayout.
                    displayView.addView(customView)

                    /// Adding its ID to the HashSet
                    addedActivities.add(currentActivityId)

                }
            }
        }

        tilStartDate.setEndIconOnClickListener {
            showDatePickerDialog(txtStartDate)
        }

        tilEndDate.setEndIconOnClickListener(){
            showDatePickerDialog(txtEndDate)
        }
            btnFilterActivities.setOnClickListener(){
                //filterActivities(txtEndDate,txtStartDate)
                var category = ToolBox.CategoryManager.getCategoryByID(intent.getIntExtra("categoryID", 1))
                var date1 = ToolBox.CategoryManager.parseDateString(txtStartDate.text.toString())
                var date2 = ToolBox.CategoryManager.parseDateString(txtEndDate.text.toString())
                var activitiesForCategory = ToolBox.CategoryManager.getActivitiesForCategory(category.catID.toString())
                var activityList = ToolBox.CategoryManager.getActivitiesForCategoryBetweenDates(activitiesForCategory, date1, date2)

                var activityView = findViewById<LinearLayout>(R.id.ActivityView)
                activityView.removeAllViews()
                for(activity in activityList){
                    val customView = custom_activity_icon(this)
                    customView.setActID(activity.actID)
                    customView.setActName(activity.title)
                    displayView.addView(customView)
                }
            }
    }

    //............................................................................................//


    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {

        when(item.itemId)
        {
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

    private fun showDatePickerDialog(textField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the text field with the selected date
                val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                textField.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    //-----------------------------------------------------------------------------------------
    private fun filterActivities(txtEndDate:EditText,txtStartDate: EditText){
        /// Creating a HashSet for the activities.
        val addedActivities = HashSet<Int>()
        val categoryList = ToolBox.CategoryManager.getCategoryList()
        val displayView = findViewById<LinearLayout>(R.id.ActivityView)
        val catID = intent.getIntExtra("categoryID", 1) // Default value if category ID is not provided
        //getting selected dates or disabled if none selected
        var selectedEndDate = txtEndDate.text.toString()
        var selectedStartDate = txtStartDate.text.toString()
        val totalCategoryHours = findViewById<TextView>(R.id.totalCategoryHours)
        val catName = intent.getStringExtra("categoryName")
        val imgResource = intent.getIntExtra("imageIcon", 0)
        val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)
        //val categoryIconImageView = findViewById<ImageView>(R.id.iconPicture)
        val categoryIdentification = findViewById<TextView>(R.id.categoryId)

        categoryNameTextView.text = catName
        //categoryIconImageView.setImageResource(imgResource)
        categoryIdentification.text = catID.toString()
        if(selectedEndDate != "" && selectedStartDate != "") {
            displayView.removeAllViews()
            for (category in categoryList) {
                val activities = ToolBox.CategoryManager.getActivitiesForCategory(catID.toString())

                for (activity in activities) {
                    if (activity.categoryId == catID && activity.category == catName && activity.startDate > selectedStartDate
                        && activity.endDate < selectedEndDate
                    ) {
                        val currentActivityId = activity.actID
                        /// Preventing duplication of an activity in the linearlayout
                        if (!addedActivities.contains(currentActivityId)) {
                            //val imageResource = resources.getIdentifier("home_icon", "drawable", packageName)
                            val customView = custom_activity_icon(this)
                            // Set activity ID and name
                            customView.setActID(activity.actID)
                            customView.setActName(activity.title)
                            // Set the bitmap image
                            //customView.setIcon(imageResource)
                            /*activity.actImage?.let { bitmap ->
                                customView.setIcon(bitmap)
                            }*/
                            displayView.addView(customView)
                            // Adding its ID to the HashSet
                            addedActivities.add(currentActivityId)
                        }
                    }
                }
            }
        }
    }
}

//.........................................EndOfFile..............................................//
