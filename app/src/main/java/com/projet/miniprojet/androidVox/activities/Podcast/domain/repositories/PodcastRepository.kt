package com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories

import com.projet.miniprojet.androidVox.activities.Podcast.domain.models.PodcastSearch


interface PodcastRepository {

    suspend fun searchPodcasts(
        query: String,
        type: String,
    ): Either<Failure, PodcastSearch>
}