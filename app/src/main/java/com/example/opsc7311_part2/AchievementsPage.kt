package com.example.opsc7311_part2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityAchievementsPageBinding
import com.google.android.material.navigation.NavigationView




class AchievementsPage : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityAchievementsPageBinding
    private val activityCompletedList = mutableListOf<Boolean>()
    private val completedActivityList = mutableListOf<Activity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //--------------------------------------Testing------------------------------------------///

        val activityList = ToolBox.DBManager.getActivitiesFromDB()
        val displayView = findViewById<LinearLayout>(R.id.achievementsLayout)

        for (activity in activityList) {
            val duration = activity.duration
            val savedTimeSpent = activity.savedTimeSpent

            //Comparing the time saved for an activity to the max time set by the user
            if(savedTimeSpent >= duration){
                val displayView = findViewById<LinearLayout>(R.id.achievementsLayout)
                val customView = custom_activity_icon(this)
                customView.setActID(activity.actID)
                customView.setActName(activity.title)
                displayView.addView(customView)

            }
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

            R.id.nav_schedule -> {
                val intent = Intent(applicationContext, Schedule::class.java)
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

}