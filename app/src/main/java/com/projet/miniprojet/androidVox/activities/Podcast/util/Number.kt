package com.projet.miniprojet.androidVox.activities.Podcast.util

fun Long.toDurationMinutes(): String {
    val minutes = (this / 60).toInt()

    return "$minutes min"
}