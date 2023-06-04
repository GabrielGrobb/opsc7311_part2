package com.example.opsc7311_part2

import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class ToolBox
{
    data class CategoryDataClass(
        val name: String,
        val icon: ImageView,
        //val color: String
        )

    data class ActivityDataClass(
        val actTitle: String,
        val actClient: String,
        val actLocation:String,
        val actCategory : CategoryDataClass,
        val actDuration : Double,
        val actStartDate: String,
        val actEndDate: String
    )


    object ActivityManager{
        private val activityList = mutableListOf<ActivityDataClass>()

        fun addActivity(activity: ActivityDataClass) {
            activityList.add(activity)
        }

        fun getActivityList(): List<ActivityDataClass> {
            return activityList
        }
    }
    /*companion object
    {
        var categoryName: String = ""

    }*/

    object CategoryManager {
        private val categoryList = mutableListOf<CategoryDataClass>()

        fun addCategory(category: CategoryDataClass) {
            categoryList.add(category)
        }

        fun getCategoryList(): List<CategoryDataClass> {
            return categoryList
        }

        //Returns the current Date as a String
        fun getCurrentDateString(): String{
            val TimeCalendar = Calendar.getInstance().time
            //Creating the Format for the date so the page will just show the date without time or timezone info
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(TimeCalendar.time)
            return formattedDate
        }
    }
}