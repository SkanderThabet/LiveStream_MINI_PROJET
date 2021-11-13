package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.OTP.OTPFirstStep

class Sign_Up : AppCompatActivity() {

    private lateinit var SignInButton : Button
    private lateinit var SignUpButton : Button
    private lateinit var username_et :EditText
    private lateinit var password_et :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        SignUpButton = findViewById(R.id.SignUpButton)
        username_et  = findViewById(R.id.username_et)
        password_et  = findViewById(R.id.password_et)


        SignUpButton.setOnClickListener{

            val email = username_et.text.toString().trim()
            val password = password_et.text.toString().trim()

            if (email.isEmpty()){
                username_et.error ="Email Required"
                return@setOnClickListener

            }
            else if (password.isEmpty()) {
                password_et.error = "password Required"
                return@setOnClickListener
            }

            else if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                Toast.makeText(this, "Email is valid", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, OTPFirstStep::class.java);
                intent.putExtra("data","test data")
                startActivity(intent)

            }else{
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            }




        }

    }



}