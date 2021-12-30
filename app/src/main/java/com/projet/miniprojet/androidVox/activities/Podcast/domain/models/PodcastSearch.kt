package com.projet.miniprojet.androidVox.activities.Podcast.domain.models

data class PodcastSearch(
    val count: Long,
    val total: Long,
    val results: List<Episode>
)
