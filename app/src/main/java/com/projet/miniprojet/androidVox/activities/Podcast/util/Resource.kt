package com.projet.miniprojet.androidVox.activities.Podcast.util

import com.projet.miniprojet.androidVox.activities.Podcast.error.Failure


sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val failure: Failure) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}