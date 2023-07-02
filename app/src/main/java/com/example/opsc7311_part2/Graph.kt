package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityGraphBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout

class Graph : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityGraphBinding

    private lateinit var chartContainer: FrameLayout
    private lateinit var lineChart: LineChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///--------------------------------------------------------------------///
        /// Navigation bar
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

        ///--------------------------------------------------------------------///

        val displayGraph = findViewById<Button>(R.id.btnDisplayGraph)

        displayGraph.setOnClickListener {
            chartContainer = findViewById(R.id.chartContainer)
            lineChart = LineChart(this)

            val categoryList: List<ToolBox.CategoryDataClass> = ToolBox.CategoryManager.getCategoryList()// your list of CategoryDataClass objects

            // Calculate the activity time spent values
            val dataPoints = categoryList.map { category ->
                category.activityTimeSpent.toHours().toFloat() // Convert duration to hours and float
            }

            lineChart.setDataPoints(dataPoints)

            chartContainer.addView(lineChart)

            println(dataPoints)
            println(ToolBox.CategoryManager.getCategoryList().size)
            println(ToolBox.AccountManager.getSettingsObject().maxHours)
            println(ToolBox.CategoryManager.getCategoryByID(1).activityTimeSpent)
        }

        /*val tilEndDate: TextInputLayout = findViewById(R.id.til_EndDate)

        tilEndDate.setEndIconOnClickListener() {
            *//*chartContainer = findViewById(R.id.chartContainer)
            lineChart = LineChart(this)

            val dataPoints = listOf(1f, 6f, 3f, 2f, 4f)
            lineChart.setDataPoints(dataPoints)

            chartContainer.addView(lineChart)*//*

            chartContainer = findViewById(R.id.chartContainer)
            lineChart = LineChart(this)

            val categoryList: List<ToolBox.CategoryDataClass> = ToolBox.CategoryManager.getCategoryList()// your list of CategoryDataClass objects

            // Calculate the activity time spent values
            val dataPoints = categoryList.map { category ->
                category.activityTimeSpent.toHours().toFloat() // Convert duration to hours and float
            }

            // Create a list of category count labels
            *//*val categoryCountLabels = ArrayList<String>()
            for (i in categoryList.indices) {
                categoryCountLabels.add("Category ${i + 1}")
            }*//*


            // Update the x-axis labels
            //lineChart.xAxisLabels = categoryCountLabels.size // Set the category count labels for the x-axis

            lineChart.setDataPoints(dataPoints)

            chartContainer.addView(lineChart)

            println(dataPoints)
            println(ToolBox.CategoryManager.getCategoryList().size)
            println(ToolBox.AccountManager.getSettingsObject().maxHours)
            println(ToolBox.CategoryManager.getCategoryByID(1).activityTimeSpent)
        }*/



    }

    //............................................................................................//

    private fun TimeOfMaxHrs() {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
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
}