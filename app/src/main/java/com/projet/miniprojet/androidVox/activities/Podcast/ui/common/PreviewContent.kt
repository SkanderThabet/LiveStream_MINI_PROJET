package com.projet.miniprojet.androidVox.activities.Podcast.ui.common

import androidx.compose.runtime.Composable
import com.projet.miniprojet.androidVox.activities.Podcast.ui.navigation.ProvideNavHostController
import com.projet.miniprojet.androidVox.activities.Podcast.ui.theme.PodcastAppTheme
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun PreviewContent(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    PodcastAppTheme(darkTheme = darkTheme) {
        ProvideWindowInsets {
            ProvideNavHostController {
                content()
            }
        }
    }
}