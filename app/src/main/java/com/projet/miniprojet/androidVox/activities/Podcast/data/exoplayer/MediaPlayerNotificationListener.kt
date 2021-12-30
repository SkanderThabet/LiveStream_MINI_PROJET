package com.projet.miniprojet.androidVox.activities.Podcast.data.exoplayer

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.fabirt.podcastapp.constant.K
import com.projet.miniprojet.androidVox.activities.Podcast.data.service.MediaPlayerService
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.projet.miniprojet.androidVox.activities.Podcast.Constant.K

class MediaPlayerNotificationListener(
    private val mediaService: MediaPlayerService
) : PlayerNotificationManager.NotificationListener {

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        mediaService.apply {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)
        mediaService.apply {
            if (ongoing || !isForegroundService) {
                ContextCompat.startForegroundService(
                    this,
                    Intent(applicationContext, this::class.java)
                )
                startForeground(K.PLAYBACK_NOTIFICATION_ID, notification)
                isForegroundService = true
            }
        }
    }
}