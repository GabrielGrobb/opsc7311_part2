package com.example.opsc7311_part2

import android.icu.text.SimpleDateFormat
import java.util.*

class Validation {
    // String validation where numbers/letters/white spaces are allowed
    fun validateStringsWithNumbers(string: String): Boolean {
        val pattern = Regex("^[a-zA-Z0-9\\s.]*$")
        return pattern.matches(string)
    }

    //............................................................................................//
    // String Validation where only letters/white spaces are allowed
    fun validateStringsNoNumbers(string:String):Boolean{
        val pattern = Regex("^[a-zA-Z\\s]+$")
        return pattern.matches(string)
    }

    //............................................................................................//
    // Date validation to check if end date is after start date
    fun isEndDateAfterStartDate(startDate: String, endDate: String): Boolean {
        if (startDate.isEmpty() || endDate.isEmpty() || startDate == " " || endDate == " ") {
            return false
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val start = dateFormat.parse(startDate)
            val end = dateFormat.parse(endDate)
            if(end >= start){
                return true
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //............................................................................................//
    // Email validation
    fun isValidEmail(email: String): Boolean {
        val pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(Regex(pattern))
    }

    //............................................................................................//

}
//.........................................EndOfFile..............................................//