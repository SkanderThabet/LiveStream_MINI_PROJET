package com.projet.miniprojet.androidVox.app

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.projet.miniprojet.R
import android.content.Intent
import android.os.Handler
import com.projet.miniprojet.androidVox.activities.OTP.OTPFirstStep


class LaunchScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this.applicationContext, OTPFirstStep::class.java))
        finish()
    }
}