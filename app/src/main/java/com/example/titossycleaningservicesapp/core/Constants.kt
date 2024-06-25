package com.example.titossycleaningservicesapp.core

import java.time.format.DateTimeFormatter

val dateTimeUiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
val dateUiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
val timeUiFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")