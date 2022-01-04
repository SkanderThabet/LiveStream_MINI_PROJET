package com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.util.ThemeHelper
import com.projet.miniprojet.androidVox.databinding.ActivityHomepageTestBinding
import io.getstream.chat.android.client.ChatClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageTestBinding
    private lateinit var themeHelper: ThemeHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupBottomNav()
    }

    private fun setupToolbar() {
        ChatClient.instance().getCurrentUser()?.let {
//            binding.userAvatarView.setUserData(it)
        }
        binding.userAvatarView.setOnClickListener {
            startActivity(HomePage.createIntent(this))
        }

        themeHelper = ThemeHelper(applicationContext)
        binding.themeSwitchImageView.setOnClickListener {
            themeHelper.toggleDarkTheme()
        }
    }

    private fun setupBottomNav() {
        with(binding.bottomNavigationView) {
            // disable reloading fragment when clicking again on the same tab
            setOnNavigationItemReselectedListener {}

            val navController = findNavController(R.id.navHostFragment)
            setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                val titleRes = when (destination.id) {
                    R.id.navigation_overview -> R.string.title_overview
                    R.id.navigation_events -> R.string.title_events
                    else -> throw IllegalArgumentException("Unsupported navigation item")
                }

                binding.toolbarTitleTextView.text = getString(titleRes)
            }
        }
    }
    private fun startHomeAct() {
        startActivity(Intent(this, HomePage::class.java))
        finish()

    }


    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
