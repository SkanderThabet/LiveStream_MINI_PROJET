package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.TAG
import com.projet.miniprojet.androidVox.activities.OTP.OTPFirstStep
import com.projet.miniprojet.androidVox.voxApp
import io.realm.mongodb.Credentials
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class Sign_Up : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SignUpButton.setOnClickListener { login(true) }
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess() {
        // successful login ends this activity, bringing the user back to the project activity
        startHomeActivity()
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, OTPFirstStep::class.java))
        finish()
    }

    private fun onLoginFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.

        (tf_username.text.toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(tf_username.text.toString())
            .matches()) -> {
            tf_username.error = "Email Required"
            false
        }
        tf_password.text.toString().isEmpty() -> {
            tf_password.error = "password Required"
            false
        }

        else -> true

    }

    // handle user authentication (login) and account creation
    private fun login(createUser: Boolean) {
        if (!validateCredentials()) {
            onLoginFailed("Invalid username or password")
            return
        }

        // while this operation completes, disable the buttons to login or create a new account
        SignUpButton.isEnabled = false

        val username = this.tf_username.text.toString()
        val password = this.tf_password.text.toString()


        if (createUser) {
            // register a user using the Realm App we created in the TaskTracker class
            voxApp.emailPassword.registerUserAsync(username, password) {
                // re-enable the buttons after user registration returns a result
                SignUpButton.isEnabled = true

                if (!it.isSuccess) {
                    onLoginFailed("Could not register user.")
                    Log.e(TAG(), "Error: ${it.error}")
                } else {
                    Log.i(TAG(), "Successfully registered user.")
                    // when the account has been created successfully, log in to the account
                    login(false)
                }
            }
        } else {
            val creds = Credentials.emailPassword(username, password)
            voxApp.loginAsync(creds) {
                // re-enable the buttons after user login returns a result
                SignUpButton.isEnabled = true
                if (!it.isSuccess) {
                    onLoginFailed(it.error.message ?: "An error occurred.")
                } else {
                    onLoginSuccess()
                }
            }
        }
    }
}



