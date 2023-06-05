package com.example.opsc7311_part2

import android.content.Context
import android.content.Intent

fun openIntent(context: Context, category: String, activityToOpen: Class<*>)
{
    // declare intent with context and class to pass the value to
    val intent = Intent(context, activityToOpen)

    // pass through the string value with key "order"
    intent.putExtra("category", category)

    // start the activity
    context.startActivity(intent)
}