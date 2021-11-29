package com.projet.miniprojet.androidVox

import android.app.Application
import com.projet.miniprojet.androidVox.R
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.livedata.ChatDomain
import io.realm.Realm
import io.realm.RealmConfiguration

const val APP_ID="vox-kpgyp"

class ChatClient : Application() {

    override fun onCreate() {
        super.onCreate()
        val client = ChatClient.Builder(getString(R.string.api_key),this).logLevel(ChatLogLevel.ALL).build()
        ChatDomain.Builder(client,this).build()

        Realm.init(this)
        val config = RealmConfiguration.Builder().name(APP_ID).build()
        val backgroundThreadRealm : Realm = Realm.getInstance(config)
    }
}