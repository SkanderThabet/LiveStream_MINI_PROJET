package com.projet.miniprojet.androidVox.activities.Homepage

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.BroadcastStreaming.BroadcastMain
import com.projet.miniprojet.androidVox.activities.Chat.ChatMain
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.MainActivity
import com.projet.miniprojet.androidVox.activities.PodcastV2.ui.PodcastActivity
import com.projet.miniprojet.androidVox.activities.SignInUp.oAuths
import com.projet.miniprojet.androidVox.other.SharedPref
import kotlinx.android.synthetic.main.activity_home_page.*
class HomePage : AppCompatActivity() {
    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        sharedPref= SharedPref(this)

        /**
         * Anonymous logging test
         */
//        val app = App(AppConfiguration.Builder(APP_ID).build())
//        val credentials=Credentials.anonymous()
//        app.loginAsync(credentials){
//            if(it.isSuccess){
//                Log.v("User","Logged in anonymously")
//            }
//            else{
//                Log.e("EXAMPLE", "Failed to log in: ${it.error.errorMessage}")
//            }
//        }

        topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }
        signoutBtnHome.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("Yes") { _, _ ->
                sharedPref.clear()
                startActivity(Intent(this,oAuths::class.java))
                finish()
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Logout?")
            builder.setMessage("Are you sure you want to logout?")
            builder.create().show()
        }
        navigationViewHome.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            if(menuItem.itemId==R.id.messages_menu){
                startChatAct()
            }
            if(menuItem.itemId==R.id.channels_menu){
                startStreamingView()
            }
            if(menuItem.itemId==R.id.podcasts_menu){
                startPodcast()
            }


            menuItem.isChecked = true

            drawerLayout.close()
            true
        }
        StreamBtn.setOnClickListener {
            startStreamingActivity()
        }

    }

    private fun startPodcast() {
        startActivity(Intent(this,PodcastActivity::class.java))
    }


    private fun startStreamingView() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun startStreamingActivity() {
        startActivity(Intent(this, BroadcastMain::class.java))
        finish()
    }


    private fun startChatAct() {
        startActivity(Intent(this, ChatMain::class.java))
        finish()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, HomePage::class.java)
        }
    }
}