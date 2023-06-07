package com.example.opsc7311_part2

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import android.text.style.BackgroundColorSpan
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import java.io.Serializable
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class ToolBox
{
    //------------------Data Classes and Objects
    data class ActivityDataClass(
        val actID:Int,
        val title: String,
        val client: String,
        val location: String,
        val category: String,
        val categoryId: Int, // ID of the category
        val duration: Duration,
        var progressionBar: ProgressionBar,
        var currentTimeSpent: Duration,
        var savedTimeSpent: Duration,
        val startDate: String,
        val endDate: String,
        val actImage: Bitmap?
    )

    data class CategoryDataClass(
        var catID : Int,
        var name: String,
        var activityTimeSpent: Duration,
        val activities: MutableList<ActivityDataClass>,
        val catColor: Int
        )

    data class AccountSettings(
        var userImage: String,
        var minHours: Int,
        var maxHours: Int,
        var email: String,
        var username: String,
        var firstName: String,
        var surname: String,
        var password: String
    )
    {
        fun updateSettings(
            userImage: String,
            minHours: Int,
            maxHours: Int,
            email: String,
            username: String,
            firstName: String,
            surname: String,
            password: String
        ) {
            this.userImage = userImage
            this.minHours = minHours
            this.maxHours = maxHours
            this.email = email
            this.username = username
            this.firstName = firstName
            this.surname = surname
            this.password = password
        }
    }

    object AccountManager{
        private val currentSettings = AccountSettings(
            "@drawable/default_profile",
            1,
            1,
            "default@default.com",
            "default",
            "default",
            "default",
            "default")

        fun getSettingsObject() : AccountSettings{
            return currentSettings;
        }

    }

    object ActivityManager{
        private val activityList = mutableListOf<ActivityDataClass>()

        fun addActivity(activity: ActivityDataClass) {
            activityList.add(activity)
        }

        //Takes in an activityid and returns an activity object from the list if it exists
        fun getActivityObjectByID(id: Int): ActivityDataClass{
            for(activity in activityList){
                if(activity.actID==id){
                    return activity
                }
            }
            //Again gonna break the code, love you gents <3
            return activityList[-1]
        }

        fun getActivityList(): List<ActivityDataClass> {
            return activityList
        }

        fun findMaxEndDate(activityList: List<ActivityDataClass>): String? {
            var maxEndDate: String? = null

            for (activity in activityList) {
                if (maxEndDate == null || activity.endDate > maxEndDate) {
                    maxEndDate = activity.endDate
                }
            }

            return maxEndDate
        }
    }

    object CategoryManager {
        private val categoryList = mutableListOf<CategoryDataClass>()
        //private val activityList = mutableListOf<ActivityDataClass>()

        //Takes in a category object and returns a duration representing the total amount of
        //Time spent on that category
        fun calcCategoryTime(cat: CategoryDataClass): Duration {
            var totalDuration = Duration.ZERO
            for (activity in cat.activities) {
                totalDuration = totalDuration.plus(activity.currentTimeSpent)
            }
            return totalDuration
        }

        //Takes in a category id and returns the category object from the list if it exists
        fun getCategoryByID(id: Int): CategoryDataClass{
            for(category in categoryList){
                if(category.catID==id){
                    return category
                }
            }
            //Not smart enough to fix will crash app sorry gents
            return categoryList[-1]
        }

        fun addCategory(category: CategoryDataClass) {
            categoryList.add(category)
        }

       /* fun addActivity(activity: ActivityDataClass){
            activityList.add(activity)
        }*/

        fun getCategoryList(): List<CategoryDataClass> {
            return categoryList
        }

        //--------General Utility Functions

        //Returns the current Date as a String
        fun getCurrentDateString(): String{
            val TimeCalendar = Calendar.getInstance().time
            //Creating the Format for the date so the page will just show the date without time or timezone info
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(TimeCalendar.time)
            return formattedDate
        }

        //Takes in a spinner and an int and returns the index of the int in the spinner if it exists
        fun getSpinnerIndexForValue(spinner: Spinner, value: String): Int{
            val adapter = spinner.adapter

            for (index in 0 until adapter.count) {
                val item = adapter.getItem(index)
                if (item.equals(value)) {
                    return index
                }
            }

            return -1 // Return -1 if the desired value is not found
        }

        //Takes in an activity object and returns a percentage value as an integer related to how far through the max length the activity is
        fun calcPercentageTimeSpent(activity: ActivityDataClass): Int {
            val totalTime = activity.duration.toMillis() // Total duration in milliseconds
            val currentTimeSpent = activity.currentTimeSpent.toMillis() // Current time spent in milliseconds

            if (totalTime > 0) {
                val percentage = (currentTimeSpent.toFloat() / totalTime.toFloat()) * 100
                return percentage.toInt()
            }

            // Return 0 if the total time is zero (to avoid division by zero)
            return 0
        }


    }
}