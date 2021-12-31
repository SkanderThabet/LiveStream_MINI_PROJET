package com.projet.miniprojet.androidVox.activities.Podcast.ui
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.projet.miniprojet.androidVox.activities.Podcast.ui.welcome.WelcomeScreen

import com.google.accompanist.insets.ProvideWindowInsets
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.activities.Podcast.Constant.K
import com.projet.miniprojet.androidVox.activities.Podcast.ui.common.ProvideMultiViewModel
import com.projet.miniprojet.androidVox.activities.Podcast.ui.home.HomeScreen
import com.projet.miniprojet.androidVox.activities.Podcast.ui.navigation.Destination
import com.projet.miniprojet.androidVox.activities.Podcast.ui.navigation.Navigator
import com.projet.miniprojet.androidVox.activities.Podcast.ui.navigation.ProvideNavHostController
import com.projet.miniprojet.androidVox.activities.Podcast.ui.podcast.PodcastBottomBar
import com.projet.miniprojet.androidVox.activities.Podcast.ui.podcast.PodcastDetailScreen
import com.projet.miniprojet.androidVox.activities.Podcast.ui.podcast.PodcastPlayerScreen
import com.projet.miniprojet.androidVox.activities.Podcast.ui.theme.PodcastAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPodcastActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Podcast)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        var startDestination = Destination.home
        if (intent?.action == K.ACTION_PODCAST_NOTIFICATION_CLICK) {
            startDestination = Destination.home
        }

        setContent {
            PodcastApp(
                startDestination = startDestination,
                backDispatcher = onBackPressedDispatcher
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PodcastApp(
    startDestination: String = Destination.home,
    backDispatcher: OnBackPressedDispatcher
) {
    PodcastAppTheme {
        ProvideWindowInsets {
            ProvideMultiViewModel {
                ProvideNavHostController {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        NavHost(Navigator.current, startDestination) {
                            composable(Destination.welcome) { WelcomeScreen() }

                            composable(Destination.home) {
                                HomeScreen()
                            }

                            composable(
                                Destination.podcast,
                                deepLinks = listOf(navDeepLink { uriPattern = "https://www.listennotes.com/e/{id}" })
                            ) { backStackEntry ->
                                PodcastDetailScreen(
                                    podcastId = backStackEntry.arguments?.getString("id")!!,
                                )
                            }
                        }
                        PodcastBottomBar(
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                        PodcastPlayerScreen(backDispatcher)
                    }
                }
            }
        }
    }
}