package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projet.miniprojet.androidVox.R
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
        startActivity(Intent(this,Sign_Up::class.java))
        finish()
    }
}