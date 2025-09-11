package com.example.mnewsapp.domain.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.LocalDate

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(input: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(input)
        zonedDateTime.toLocalDate().toString() // returns "2025-07-27"
    } catch (e: Exception) {
        "Unknown Date"
    }
}
