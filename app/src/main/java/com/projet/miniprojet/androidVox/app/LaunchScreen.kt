package com.projet.miniprojet.androidVox.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projet.miniprojet.androidVox.activities.Chat.ChatMain
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.activities.OTP.OTPFirstStep
import com.projet.miniprojet.androidVox.activities.SignInUp.Sign_Up
import com.projet.miniprojet.androidVox.activities.SignInUp.oAuths
import kotlinx.android.synthetic.main.activity_sign_up.*


class LaunchScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this.applicationContext, OnBoardingActivity::class.java))
        finish()
    }
}