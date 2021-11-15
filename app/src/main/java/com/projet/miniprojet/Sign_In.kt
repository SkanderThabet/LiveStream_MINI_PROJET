package com.projet.miniprojet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.InputBinding
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

class Sign_In : AppCompatActivity() {

    private lateinit var SignInButton : Button
    private lateinit var textinputEmail :TextInputEditText
    private lateinit var textinputpass :TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sign_in)


        SignInButton = findViewById(R.id.SignInButton)
        textinputEmail  = findViewById(R.id.textinputEmail)
        textinputpass  = findViewById(R.id.textinputpass)


        SignInButton.setOnClickListener{

            val email = textinputEmail.text.toString()
            val password = textinputpass.text.toString()

            if (email.isEmpty()){

                textinputEmail.error ="Email Required"
            }
            else if (password.isEmpty()) {
                textinputpass.error = "password Required"

            }
            else if (password.length<8) {
                textinputpass.error = "Password must be more than 6 caracteres"
            }
            else if (!password.matches(".*[A-Z].*".toRegex())) {
                textinputpass.error = "Must Contain 1 Upper-case Character"
            } else if (!password.matches(".*[a-z].*".toRegex())) {
                textinputpass.error = "Must Contain 1 Lower-case Character"
            } else if (!password.matches(".*[0-9].*".toRegex())) {
                textinputpass.error = "Must Contain 1 Number"
            }

            else if (email.isNotEmpty() && password.isNotEmpty()  && Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                Toast.makeText(this, "valid Email", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Sign_In, OTP::class.java)
                    startActivity(intent)




            }else{
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()

            }





        }

    }



}