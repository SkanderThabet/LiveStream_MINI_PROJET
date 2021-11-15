package com.projet.miniprojet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class Profile : AppCompatActivity() {

    private lateinit var button_complete: Button
    private lateinit var yourusername: TextView
    private lateinit var yourname: TextView
    private lateinit var yourlastname: Text
    private lateinit var yourbirthday: Text


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        button_complete = findViewById(R.id.button_complete)
        button_complete.setOnClickListener {

//            if (yourusername.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
//                    email
//                ).matches()
//            ) {
//
//
//            }
        }
    }
}