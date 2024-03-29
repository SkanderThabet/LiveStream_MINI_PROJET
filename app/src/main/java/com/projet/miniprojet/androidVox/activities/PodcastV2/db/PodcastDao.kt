package com.projet.miniprojet.androidVox.activities.PodcastV2.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.projet.miniprojet.androidVox.activities.PodcastV2.model.Episode
import com.projet.miniprojet.androidVox.activities.PodcastV2.model.Podcast

@Dao
interface PodcastDao{
    @Query("SELECT * FROM Podcast ORDER BY feedTitle")
    fun loadPodcast(): LiveData<List<Podcast>>

    @Query("SELECT * FROM Episode WHERE podcastId = :podcastId ORDER BY releaseDate DESC")
    fun loadEpisdoes(podcastId: Long): List<Episode>

    @Insert(onConflict = REPLACE)
    fun insertPodcast(podcast : Podcast): Long

    @Insert(onConflict = REPLACE)
    fun insertEpisode(edisode : Episode) : Long

    @Query("SELECT *  FROM Podcast WHERE feedUrl = :url")
    fun loadPodcast(url : String): Podcast?

    @Delete()
    fun deletePodcast(podcast: Podcast)

    @Query("SELECT * FROM Podcast ORDER BY feedTitle")
    fun loadPodcastsStatic(): List<Podcast>

}