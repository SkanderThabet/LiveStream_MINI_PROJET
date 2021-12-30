package com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories


import com.projet.miniprojet.androidVox.activities.Podcast.data.datastore.PodcastDataStore
import com.projet.miniprojet.androidVox.activities.Podcast.data.network.service.PodcastService
import com.projet.miniprojet.androidVox.activities.Podcast.domain.models.PodcastSearch
import com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories.PodcastRepository

class PodcastRepositoryImpl(
    private val service: PodcastService,
    private val dataStore: PodcastDataStore
) : PodcastRepository {

    companion object {
        private const val TAG = "PodcastRepository"
    }

    override suspend fun searchPodcasts(
        query: String,
        type: String
    ): Either<Failure, PodcastSearch> {
        return try {
            val canFetchAPI = dataStore.canFetchAPI()
            if (canFetchAPI) {
                val result = service.searchPodcasts(query, type).asDomainModel()
                dataStore.storePodcastSearchResult(result)
                right(result)
            } else {
                right(dataStore.readLastPodcastSearchResult())
            }
        } catch (e: Exception) {
            left(Failure.UnexpectedFailure)
        }
    }
}