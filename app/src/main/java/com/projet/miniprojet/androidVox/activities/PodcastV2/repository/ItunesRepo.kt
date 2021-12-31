package com.projet.miniprojet.androidVox.activities.PodcastV2.repository

import com.projet.miniprojet.androidVox.activities.PodcastV2.Service.ItunesService
import com.projet.miniprojet.androidVox.activities.PodcastV2.Service.PodcastResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItunesRepo(private val itunesService: ItunesService) {
    fun searchByTerm(
        term: String,
        callBack: (List<PodcastResponse.ItunesPodcast>?) -> Unit) {

        val podcastCall = itunesService.searchPodcastByTerm(term)

        podcastCall.enqueue(object : Callback<PodcastResponse> {

            override fun onFailure(
                call: Call<PodcastResponse>?,
                t: Throwable?
            ) {
                callBack(null)
            }

            override fun onResponse(
                call: Call<PodcastResponse>?,
                response: Response<PodcastResponse>?
            ) {

                val body = response?.body()
                callBack(body?.results)
            }
        })
    }
}