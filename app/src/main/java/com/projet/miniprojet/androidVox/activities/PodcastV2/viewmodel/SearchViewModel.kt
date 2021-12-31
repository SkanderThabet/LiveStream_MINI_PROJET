package com.projet.miniprojet.androidVox.activities.PodcastV2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.projet.miniprojet.androidVox.activities.PodcastV2.Service.PodcastResponse
import com.projet.miniprojet.androidVox.activities.PodcastV2.repository.ItunesRepo
import com.projet.miniprojet.androidVox.activities.PodcastV2.util.DateUtils

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var itunesRepo: ItunesRepo? = null

    data class PodcastSummaryViewData(val name: String? = "", val lastUpdated: String? = "", val imageUrl: String? = "", val feedUrl: String? = "")

    private fun itunesPodcastToPodcastSummaryView(itunesPodcast: PodcastResponse.ItunesPodcast) : PodcastSummaryViewData {
        return PodcastSummaryViewData(itunesPodcast.collectionCensoredName, DateUtils.JsonDateToShortDate(itunesPodcast.releaseDate), itunesPodcast.artworkUrl100, itunesPodcast.feedUrl)
    }

    fun searchPodcast(term: String, callback:(List<PodcastSummaryViewData>) -> Unit){
        itunesRepo?.searchByTerm(term) { results ->
            if(results == null){
                callback(emptyList())
            }else{
                val searchViews = results.map{podcast -> itunesPodcastToPodcastSummaryView(podcast)}
                callback(searchViews)
            }
        }
    }
}