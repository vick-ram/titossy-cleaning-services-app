package com.example.titossycleaningservicesapp.core

import android.content.Context
import android.widget.Toast
import java.time.format.DateTimeFormatter

val dateTimeUiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
val dateUiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
val timeUiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

const val app_id = "03cde78b-a63d-4c07-86d7-7f997aa57832"

fun showToast(
    context: Context,
    length: Int = Toast.LENGTH_SHORT,
    message: String
) {
    Toast.makeText(
        context,
        message,
        length
    ).show()
}