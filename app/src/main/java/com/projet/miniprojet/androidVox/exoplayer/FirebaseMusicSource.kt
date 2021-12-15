package com.projet.miniprojet.androidVox.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.projet.miniprojet.androidVox.exoplayer.State.*
import com.projet.miniprojet.androidVox.remote.MusicDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val musicDatabase: MusicDatabase
) {

     var songs = emptyList<MediaMetadataCompat>()

    //Switching the currently running thread of the coroutine to the IO thread which is more optimized
    suspend fun fetchMediaData() = withContext(Dispatchers.IO){
        state = STATE_INITIALIZING
        val allSongs = musicDatabase.getAllSongs()
        songs = allSongs.map { song ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_ARTIST,song.description)
                .putString(METADATA_KEY_MEDIA_ID,song.mediaId)
                .putString(METADATA_KEY_TITLE,song.title)
                .putString(METADATA_KEY_DISPLAY_TITLE,song.title)
                .putString(METADATA_KEY_DISPLAY_ICON_URI,song.imageURL)
                .putString(METADATA_KEY_MEDIA_URI,song.songURL)
                .putString(METADATA_KEY_ART_URI,song.imageURL)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION,song.description)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE,song.description)
                .build()
        }
        state=STATE_INITIALIZED
    }
    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory) : ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        songs.forEach { song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = songs.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc,FLAG_PLAYABLE)
    }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state:State = STATE_CREATED
    set(value){
        if(value== STATE_INITIALIZED || value == STATE_ERROR){
            synchronized(onReadyListeners){
                field=value
                onReadyListeners.forEach{listener ->
                    listener(state==STATE_INITIALIZED)
                }
            }
        }else{
            field = value
        }
    }

    fun whenReady(action: (Boolean) -> Unit):Boolean {
        if(state == STATE_CREATED || state == STATE_INITIALIZING){
            onReadyListeners +=action
            return false
        }else {
            action(state==STATE_INITIALIZED)
            return true
        }
    }

}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}