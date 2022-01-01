package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import kotlinx.android.synthetic.main.activity_oauths.*

class oAuths : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauths)

        SignInButtonWPV2.setOnClickListener {
            startSigninActivity()
        }
        signUpBtnWV2.setOnClickListener {
            startSignupActivity()
        }
    }

    private fun startSigninActivity() {
        startActivity(Intent(this,Sign_In::class.java))
        finish()
    }
    private fun startSignupActivity() {
        startActivity(Intent(this,Profile_compelation::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        val voxPref: SharedPreferences = getSharedPreferences("vox_app", MODE_PRIVATE)
        if (voxPref.contains("token")) {
            startActivity(Intent(this@oAuths, HomePage::class.java))
            finish()
        }
    }
}