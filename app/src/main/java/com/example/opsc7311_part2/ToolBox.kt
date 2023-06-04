package com.example.opsc7311_part2

import android.widget.ImageView
import android.widget.TextView

class ToolBox
{
    data class CategoryDataClass(
        val name: String,
        val icon: ImageView,
        //val color: String
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
    }
}