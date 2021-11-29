package com.projet.miniprojet.androidVox.activities.Homepage

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Chat.ChatMain
import com.projet.miniprojet.androidVox.activities.OTP.OTPSecondStep
import com.projet.miniprojet.androidVox.activities.OTP.PHONE_NUMBER
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.activity_home_page.drawerLayout
import kotlinx.android.synthetic.main.fragment_channel.*

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.open()
        }
        signoutBtnHome.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setPositiveButton("Yes") { _, _ ->
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

            menuItem.isChecked = true

            drawerLayout.close()
            true
        }
    }

    private fun startChatAct() {
        startActivity(Intent(this, ChatMain::class.java))
        finish()
    }
}