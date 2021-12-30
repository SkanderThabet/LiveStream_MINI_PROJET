package com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories

import com.projet.miniprojet.androidVox.activities.Podcast.domain.models.PodcastSearch
import com.projet.miniprojet.androidVox.activities.Podcast.error.Failure
import com.projet.miniprojet.androidVox.activities.Podcast.util.Either


interface PodcastRepository {

    suspend fun searchPodcasts(
        query: String,
        type: String,
    ): Either<Failure, PodcastSearch>
}