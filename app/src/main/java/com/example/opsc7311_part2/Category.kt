package com.example.opsc7311_part2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.opsc7311_part2.databinding.ActivityCategoryBinding
import com.google.android.material.navigation.NavigationView

class Category : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var binding: ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
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

/*      val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)
        val categoryIconImageView = findViewById<ImageView>(R.id.iconPicture)*/

       /* val catName = intent.getStringExtra("categoryName")
        val imgResource = intent.getIntExtra("imageIcon", 0)

        val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)
        val categoryIconImageView = findViewById<ImageView>(R.id.iconPicture)

        categoryNameTextView.text = catName
        categoryIconImageView.setImageResource(imgResource)*/

        val catName = intent.getStringExtra("categoryName")
        val imgResource = intent.getIntExtra("imageIcon", 0)

        val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)
        val categoryIconImageView = findViewById<ImageView>(R.id.iconPicture)

        categoryNameTextView.text = catName
        categoryIconImageView.setImageResource(imgResource)

        /*val category = intent.getParcelableExtra<ToolBox.CategoryDataClass>("category")
        if (category != null) {
            val categoryName = category.name
            val categoryIconResourceId = category.icon

            val categoryNameTextView = findViewById<TextView>(R.id.CategoryName)
            val categoryIconImageView = findViewById<ImageView>(R.id.iconPicture)

            categoryNameTextView.text = categoryName
            categoryIconImageView.setImageResource(categoryIconResourceId)
        }*/
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
                val intent = Intent(applicationContext, AchievementActivity::class.java)
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
