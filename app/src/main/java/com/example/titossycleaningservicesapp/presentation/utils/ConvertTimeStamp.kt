package com.example.titossycleaningservicesapp.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun LocalDateTime.formattedDateToString(date:Date): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formattedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        formattedDate.toString()
    } else {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date)
    }
}
