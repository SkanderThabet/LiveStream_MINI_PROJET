package com.projet.miniprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.projet.miniprojet.Retrofit.Myservice
import io.reactivex.disposables.CompositeDisposable

class Welcome : AppCompatActivity() {


    //lateinit var Myservice:Myservice
    //internal var compositeDisposable = CompositeDisposable()

    private lateinit var SignInButton : Button
    private lateinit var SignUpButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        SignInButton = findViewById(R.id.SignInButtonw)
        SignUpButton = findViewById(R.id.SignUpButtonw)

        SignInButton.setOnClickListener{

            val intent = Intent(this@Welcome, Sign_In::class.java)
            startActivity(intent)

        }

        SignUpButton.setOnClickListener{

            val intent = Intent(this@Welcome, Sign_Up::class.java)
            startActivity(intent)


        }



    }
}