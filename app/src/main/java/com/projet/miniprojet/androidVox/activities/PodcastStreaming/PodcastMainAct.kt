package com.projet.miniprojet.androidVox.activities.PodcastStreaming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.RequestManager
import com.projet.miniprojet.androidVox.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PodcastMainAct : AppCompatActivity() {

    @Inject
    lateinit var glide: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast_main)
    }
}