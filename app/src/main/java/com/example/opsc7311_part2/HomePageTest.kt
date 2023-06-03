package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityHomePageBinding
import com.example.opsc7311_part2.databinding.ActivityHomePageTestBinding
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.material.navigation.NavigationView

class HomePageTest : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityHomePageTestBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageTestBinding.inflate(layoutInflater)
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

        navButton()

    }
    //............................................................................................//

    private fun navButton()
    {
        val fabMenu: FloatingActionsMenu = findViewById(R.id.fab_menu)
        val fabCategory: FloatingActionButton = findViewById(R.id.fab_category)
        val fabActivity: FloatingActionButton = findViewById(R.id.fab_activity)

        fabCategory.setOnClickListener {
            // Perform action when the "Add Category" button is clicked
            // Navigate to the desired page for adding a category
            val intent = Intent(this, AddCategory::class.java)
            startActivity(intent)

            // Close the floating action menu after navigation
            fabMenu.collapse()
        }

        fabActivity.setOnClickListener {
            // Perform action when the "Add Activity" button is clicked
            // Navigate to the desired page for adding an activity
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)

            // Close the floating action menu after navigation
            fabMenu.collapse()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {

        when(item.itemId)
        {
            R.id.nav_schedule -> {
                val intent = Intent(applicationContext, Schedule::class.java)
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