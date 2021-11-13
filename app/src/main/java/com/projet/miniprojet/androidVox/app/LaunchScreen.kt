package com.projet.miniprojet.androidVox.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class LaunchScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this.applicationContext, OnBoardingActivity::class.java))
        finish()
    }
}