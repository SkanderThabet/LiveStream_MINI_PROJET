package com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories


import android.os.Build
import androidx.annotation.RequiresApi
import com.projet.miniprojet.androidVox.activities.Podcast.data.datastore.PodcastDataStore
import com.projet.miniprojet.androidVox.activities.Podcast.data.network.service.PodcastService
import com.projet.miniprojet.androidVox.activities.Podcast.domain.models.PodcastSearch
import com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories.PodcastRepository
import com.projet.miniprojet.androidVox.activities.Podcast.error.Failure
import com.projet.miniprojet.androidVox.activities.Podcast.util.Either
import com.projet.miniprojet.androidVox.activities.Podcast.util.left
import com.projet.miniprojet.androidVox.activities.Podcast.util.right

class PodcastRepositoryImpl(
    private val service: PodcastService,
    private val dataStore: PodcastDataStore
) : PodcastRepository {

    companion object {
        private const val TAG = "PodcastRepository"
    }

    @RequiresApi(Build.VERSION_CODES.O)
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