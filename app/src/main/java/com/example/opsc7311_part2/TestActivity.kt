package com.example.opsc7311_part2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class TestActivity : AppCompatActivity() {
    //Variables
    lateinit var drawerLayout: DrawerLayout

    lateinit var navigationView: NavigationView

    lateinit var toolbar: Toolbar

    lateinit var menu: Menu

    lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        navigationView = findViewById<NavigationView>(R.id.nav_view)

        toolbar = findViewById<Toolbar>(R.id.nav_toolbar)

        setSupportActionBar(toolbar.findViewById(R.id.nav_toolbar))

        navigationView.bringToFront()

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar.findViewById(R.id.nav_toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //navigationView.setNavigationItemSelectedListener(this)

        navigationView.setCheckedItem(R.id.nav_home)
    }

    override fun onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_home -> {}
            R.id.nav_bus -> {
                val intent = Intent(this@MainActivity, Bus::class.java)
                startActivity(intent)
            }
            R.id.nav_login -> {
                menu.findItem(R.id.nav_logout).isVisible = true
                menu.findItem(R.id.nav_account).isVisible = true
                menu.findItem(R.id.nav_login).isVisible = false
            }
            R.id.nav_logout -> {
                menu.findItem(R.id.nav_logout).isVisible = false
                menu.findItem(R.id.nav_profile).isVisible = false
                menu.findItem(R.id.nav_login).isVisible = true
            }
            R.id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }*/
}