package com.projet.miniprojet.androidVox.activities.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.SignInUp.Sign_In
import com.projet.miniprojet.androidVox.activities.SignInUp.Sign_Up

class WelcomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        val signup = findViewById<Button>(R.id.SignUPWelcome)
        val signin = findViewById<Button>(R.id.SignInButtonWelcome)

        signin.setOnClickListener {
            startActivity(Intent(this, Sign_In::class.java))
        }
        signup.setOnClickListener {
            startActivity(Intent(this, Sign_Up::class.java))
        }
    }
}