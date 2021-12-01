package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.TAG
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.voxApp
import io.realm.mongodb.Credentials
import kotlinx.android.synthetic.main.activity_sign_in.*

class Sign_In : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        SignInButton.setOnClickListener { login(false) }
    }


    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess() {
        // successful login ends this activity, bringing the user back to the project activity
        startActivity(Intent(this,HomePage::class.java))
        finish()
    }

    private fun onLoginFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        textinputEmail.text.toString().isEmpty() -> false
        textinputpass.text.toString().isEmpty() -> false
        else -> true
    }

    // handle user authentication (login) and account creation
    private fun login(createUser: Boolean) {
        if (!validateCredentials()) {
            onLoginFailed("Invalid username or password")
            return
        }

        // while this operation completes, disable the buttons to login or create a new account
        SignInButton.isEnabled = false

        val username = this.textinputEmail.text.toString()
        val password = this.textinputpass.text.toString()


        if (createUser) {
            // register a user using the Realm App we created in the TaskTracker class
            voxApp.emailPassword.registerUserAsync(username, password) {
                // re-enable the buttons after user registration returns a result
                SignInButton.isEnabled = true
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
                SignInButton.isEnabled = true
                if (!it.isSuccess) {
                    onLoginFailed(it.error.message ?: "An error occurred.")
                } else {
                    onLoginSuccess()
                }
            }
        }
    }
}