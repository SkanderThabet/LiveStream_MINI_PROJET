package com.projet.miniprojet.androidVox.activities.PodcastV2.Service

data class PodcastResponse(val resultCount: Int, val results:List<ItunesPodcast>){
    data class ItunesPodcast(val collectionCensoredName:String, val feedUrl:String, val artworkUrl100:String, val releaseDate:String)
}