package com.projet.miniprojet.androidVox.activities.Podcast.data.network.model

import com.projet.miniprojet.androidVox.activities.Podcast.domain.models.PodcastSearch


data class PodcastSearchDto(
    val count: Long,
    val total: Long,
    val results: List<EpisodeDto>
) {

    fun asDomainModel() = PodcastSearch(
        count,
        total,
        results.map { it.asDomainModel() }
    )
}
