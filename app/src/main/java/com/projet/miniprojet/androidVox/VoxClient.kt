package com.projet.miniprojet.androidVox

import android.app.Application
import android.util.Log
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.AppConfig
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.livedata.ChatDomain
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration

lateinit var voxApp: App
const val APP_ID="vox-kpgyp"
inline fun <reified T> T.TAG(): String = T::class.java.simpleName

class VoxClient : Application() {

    override fun onCreate() {
        super.onCreate()

        val client = ChatClient.Builder(getString(R.string.api_key),this).logLevel(ChatLogLevel.ALL).build()
        ChatDomain.Builder(client,this).build()
        connectUser()

        Realm.init(this)
        val config = RealmConfiguration.Builder().name(APP_ID).build()
        val backgroundThreadRealm : Realm = Realm.getInstance(config)
        voxApp = App(
            AppConfiguration.Builder(BuildConfig.MONGODB_REALM_APP_ID)
                .defaultSyncErrorHandler { session, error ->
                    Log.e(TAG(), "Sync error: ${error.errorMessage}")
                }
                .build())

        // Enable more logging in debug mode
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL)
        }

        Log.v(TAG(), "Initialized the Realm App configuration for: ${voxApp.configuration.appId}")
    }
    private fun connectUser() {
        val userCredentials = AppConfig.availableUsers[0]
        ChatClient.instance().connectUser(
            user = User(
                id = userCredentials.id,
                extraData = mutableMapOf(
                    "name" to userCredentials.name,
                    "image" to userCredentials.image
                )
            ),
            token = userCredentials.token
        ).enqueue()
    }
}