package com.example.notes_app.domain.time

import kotlinx.datetime.*


object DateTimeUtil {

    fun now() : LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochMillis(dateTime: LocalDateTime) : Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun formatDate(dateTime: LocalDateTime) : String {
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if(dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth.toString()
        val year = dateTime.year
        val hour = if(dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour.toString()
        val minute = if(dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute.toString()

        return buildString {
            append("$month $day, $year")
            append(" ")
            append("$hour:$minute")
        }
    }
}