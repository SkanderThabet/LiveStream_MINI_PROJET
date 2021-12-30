package com.projet.miniprojet.androidVox.activities.Podcast.domain.models

data class Episode (
    val id: String,
    val link: String,
    val audio: String,
    val image: String,
    val podcast: Podcast,
    val thumbnail: String,
    val pubDateMS: Long,
    val titleOriginal: String,
    val listennotesURL: String,
    val audioLengthSec: Long,
    val explicitContent: Boolean,
    val descriptionOriginal: String,
        )