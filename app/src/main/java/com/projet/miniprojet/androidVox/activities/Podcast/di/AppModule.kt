package com.projet.miniprojet.androidVox.activities.Podcast.di

import android.content.Context
import com.projet.miniprojet.androidVox.activities.Podcast.data.datastore.PodcastDataStore
import com.projet.miniprojet.androidVox.activities.Podcast.data.exoplayer.PodcastMediaSource
import com.projet.miniprojet.androidVox.activities.Podcast.data.network.client.ListenNotesAPIClient
import com.projet.miniprojet.androidVox.activities.Podcast.data.network.service.PodcastService
import com.projet.miniprojet.androidVox.activities.Podcast.data.service.MediaPlayerServiceConnection
import com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories.PodcastRepository
import com.projet.miniprojet.androidVox.activities.Podcast.domain.repositories.PodcastRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideHttpClient(): OkHttpClient = ListenNotesAPIClient.createHttpClient()

    @Provides
    @Singleton
    fun providePodcastService(
        client: OkHttpClient
    ): PodcastService = ListenNotesAPIClient.createPodcastService(client)

    @Provides
    @Singleton
    fun providePodcastDataStore(
        @ApplicationContext context: Context
    ): PodcastDataStore = PodcastDataStore(context)

    @Provides
    @Singleton
    fun providePodcastRepository(
        service: PodcastService,
        dataStore: PodcastDataStore
    ): PodcastRepository = PodcastRepositoryImpl(service, dataStore)

    @Provides
    @Singleton
    fun provideMediaPlayerServiceConnection(
        @ApplicationContext context: Context,
        mediaSource: PodcastMediaSource
    ): MediaPlayerServiceConnection = MediaPlayerServiceConnection(context, mediaSource)
}