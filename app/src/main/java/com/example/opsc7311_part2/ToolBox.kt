package com.example.opsc7311_part2

import android.os.Parcel
import android.os.Parcelable
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class ToolBox
{
    data class CategoryDataClass(
        val name: String,
        val icon: ImageView,
        val activites: List<ActivityDataClass>
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
        private val activityList = mutableListOf<ActivityDataClass>()

        fun addCategory(category: CategoryDataClass) {
            categoryList.add(category)
        }

        fun addActivity(activity: ActivityDataClass){
            activityList.add(activity)
        }

        fun getCategoryList(): List<CategoryDataClass> {
            return categoryList
        }

        fun getActivityList(): List<ActivityDataClass>{
            return activityList
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