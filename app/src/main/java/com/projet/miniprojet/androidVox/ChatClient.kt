package com.projet.miniprojet.androidVox.activities.Chat

import android.app.Application
import com.projet.miniprojet.androidVox.R
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.ChatDomain

class ChatClient : Application() {

    override fun onCreate() {
        super.onCreate()
        val client = ChatClient.Builder(getString(R.string.api_key),this).logLevel(ChatLogLevel.ALL).build()
        ChatDomain.Builder(client,this).build()
    }
}