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
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.projet.miniprojet.Retrofit.Myservice
import java.util.regex.Pattern
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.projet.miniprojet.User


class Sign_Up : AppCompatActivity() {
    private lateinit var SignUpButton : Button
    private lateinit var textinputEmail : TextInputEditText
    private lateinit var textinputpass :TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        SignUpButton = findViewById(R.id.SignUpButton)
        textinputEmail  = findViewById(R.id.tf_username)
        textinputpass  = findViewById(R.id.tf_password)


        SignUpButton.setOnClickListener{

            val email = textinputEmail.text.toString().trim()
            val password = textinputpass.text.toString().trim()

            if (email.isEmpty()){
                textinputEmail.error ="Email Required"
                return@setOnClickListener

            }
            else if (password.isEmpty()) {
                textinputpass.error = "password Required"
                return@setOnClickListener
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

            else if (email.isNotEmpty() && password.isNotEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                Toast.makeText(this, "valid Email", Toast.LENGTH_SHORT).show()

                doRegister()



            }else{
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            }





        }

    }
    private fun doRegister(){

        val apiInterface = Myservice.create()


        apiInterface.register(textinputEmail.text.toString().trim() , textinputpass.text.toString().trim()).enqueue(object :
            Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                val user = response.body()

                if (user!=null){
                    Toast.makeText(this@Sign_Up, "Registration Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Sign_Up, Profile::class.java))

                }else{
                    Toast.makeText(this@Sign_Up, "User already has an account", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@Sign_Up, "Connexion error!", Toast.LENGTH_SHORT).show()


            }

        })


    }



}