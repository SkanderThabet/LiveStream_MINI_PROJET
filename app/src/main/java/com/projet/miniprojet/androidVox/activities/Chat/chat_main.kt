package com.projet.miniprojet.androidVox.activities.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.adapters.ScreenSliderAdapter
import kotlinx.android.synthetic.main.activity_chat_main.*
import kotlinx.android.synthetic.main.activity_on_boarding.*

class chat_main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        setSupportActionBar(toolbar)
        viewPager.adapter = ScreenSliderAdapter(this)
        TabLayoutMediator(tabs, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                        0 -> tab.text= "CHATS"
                        1 -> tab.text ="PEOPLE"
                }
            }).attach()

    }
}