package com.projet.miniprojet.androidVox.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projet.miniprojet.androidVox.activities.Chat.chat_main


class LaunchScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this.applicationContext, chat_main::class.java))
        finish()
    }
}