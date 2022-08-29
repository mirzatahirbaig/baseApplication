package com.mobizion.gallary.extensions

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@Suppress("SameParameterValue")
@SuppressLint("SimpleDateFormat")
fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
    SimpleDateFormat("dd.MM.yyyy").let { formatter ->
        TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
    }


fun Long.convertSecondsToHMmSs(): String {
    val s = (this / 1000).toInt() % 60
    val m = (this / (1000 * 60) % 60)
    val h = (this / (1000 * 60 * 60) % 24)
    return if (m < 1 && h < 1) {
        String.format("%02d:%02d", m, s)
    } else if (m >= 1 && h < 1) {
        String.format("%02d:%02d", m, s)
    } else {
        String.format("%02d:%02d:%02d", h, m, s)
    }
}

/**
 * Convenience extension method to register a [ContentObserver] given a lambda.
 */
fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)
    return contentObserver
}