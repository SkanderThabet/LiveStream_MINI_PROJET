package com.projet.miniprojet.androidVox.activities.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.fragments.LoginFragmentChatDirections
import com.projet.miniprojet.androidVox.models.ChatUser
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.name

class ChatMain : AppCompatActivity() {
    private lateinit var navController:NavController
    private val client = ChatClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainchat)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

        navController=findNavController(R.id.navHostFragment)
        if(navController.currentDestination?.label.toString().contains("login")){
            val currentUser= client.getCurrentUser()
            if(currentUser!=null){
                val user =ChatUser(currentUser.name,currentUser.id)
                val action = LoginFragmentChatDirections.actionLoginFragmentChatToChannelFragment(user)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                navController.navigate(action)

            }
        }
    }

}