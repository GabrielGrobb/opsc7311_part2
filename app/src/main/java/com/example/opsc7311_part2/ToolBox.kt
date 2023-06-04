package com.example.opsc7311_part2

import android.widget.ImageView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class ToolBox
{
    data class ActivityDataClass(
        val title: String,
        val client: String,
        val location: String,
        val category: CategoryDataClass,
        val duration: Int,
        val startDate: Date,
        val endDate: Date
    )

    data class CategoryDataClass(
        val name: String,
        val icon: ImageView,
        val activites: Array<ActivityDataClass>
        )

    companion object
    {
        var categoryName: String = ""

    }

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