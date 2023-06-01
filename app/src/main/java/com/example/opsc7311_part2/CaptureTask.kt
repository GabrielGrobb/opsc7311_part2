package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityCaptureTaskBinding
import com.google.android.material.navigation.NavigationView

class CaptureTask : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityCaptureTaskBinding

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