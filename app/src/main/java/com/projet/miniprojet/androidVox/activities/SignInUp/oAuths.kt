package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import com.projet.miniprojet.androidVox.BuildConfig
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.activities.OTP.OTPFirstStep
import com.projet.miniprojet.androidVox.other.SharedPref
import kotlinx.android.synthetic.main.activity_oauths.*

const val TAG = "Vox_One_Tap"
const val EXTRA_CREDENTIAL = "Credential"
lateinit var sharedPref: SharedPref
class oAuths : AppCompatActivity() {

    private val parentView: View by lazy {
        findViewById<View>(android.R.id.content)
    }
    private lateinit var oneTapClient: SignInClient

    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {

        oneTapClient = Identity.getSignInClient(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauths)
        sharedPref = SharedPref(this)
        SignInButtonWPV2.setOnClickListener {
            startSigninActivity()
        }
        signUpBtnWV2.setOnClickListener {
            startSignupActivity()
        }

//        googleSigninBtn.setOnClickListener {
//            signIn()
//        }


    }

//    private fun signIn() {
//        val signInRequest = createSignInRequest(onlyAuthorizedAccounts = true)
//
//        oneTapClient
//            .beginSignIn(signInRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    startIntentSenderForResult(
//                        result.pendingIntent.intentSender, REQ_ONE_TAP,
//                        null, 0, 0, 0, null
//                    )
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(this) { e ->
//                Log.d(TAG, e.localizedMessage)
//                // No saved credentials found. Launch the One Tap sign-up flow
//                signUp()
//            }
//    }
//
//    private fun signUp() {
//        val signUpRequest = createSignInRequest(onlyAuthorizedAccounts = false)
//
//        oneTapClient
//            .beginSignIn(signUpRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    startIntentSenderForResult(
//                        result.pendingIntent.intentSender, REQ_ONE_TAP,
//                        null, 0, 0, 0, null
//                    )
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(this) { e ->
//                Log.d(TAG, e.localizedMessage)
//                // No saved credentials found. Show error
//                Snackbar.make(parentView, e.localizedMessage, Snackbar.LENGTH_LONG).show()
//            }
//    }
//
//    private fun createSignInRequest(onlyAuthorizedAccounts: Boolean): BeginSignInRequest =
//        BeginSignInRequest.builder()
//            .setPasswordRequestOptions(
//                BeginSignInRequest.PasswordRequestOptions.builder()
//                    .setSupported(true)
//                    .build()
//            )
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(onlyAuthorizedAccounts)
//                    .build()
//            )
//            .build()
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQ_ONE_TAP -> {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//                    val username = credential.id
//                    val password = credential.password
//                    when {
//                        idToken != null -> {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with your backend.
//                            sharedPref.setValue_string("token", idToken)
//                            Log.d(TAG, "Got ID token: $idToken")
//                        }
//                        username != null -> {
//                            Log.d(TAG, "Got name: $username")
//                        }
//                        password != null -> {
//                            // Got a saved username and password. Use them to authenticate
//                            // with your backend.
//                            Log.d(TAG, "Got password: $password")
//                        }
//                        else -> {
//                            // Shouldn't happen.
//                            Log.d(TAG, "No ID token or password!")
//                        }
//                    }
//
//                    val intent = Intent(this, HomePage::class.java)
//                        .apply {
//                            putExtra(EXTRA_CREDENTIAL, credential)
//                            val token = credential.googleIdToken
//                            sharedPref.setValue_string("token",token)
//
//                        }
//                    startActivity(intent)
//
//                } catch (e: ApiException) {
//                    when (e.statusCode) {
//                        CommonStatusCodes.CANCELED -> {
//                            Log.d(TAG, "One-tap dialog was closed.")
//                            // Don't re-prompt the user.
//                            showOneTapUI = false
//                        }
//                        CommonStatusCodes.NETWORK_ERROR -> {
//                            Log.d(TAG, "One-tap encountered a network error.")
//                            // Try again or just ignore.
//                        }
//                        else -> {
//                            Log.d(
//                                TAG, "Couldn't get credential from result." +
//                                        " (${e.localizedMessage})"
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }


    private fun startSigninActivity() {
        startActivity(Intent(this,Sign_In::class.java))
        finish()
    }
    private fun startSignupActivity() {
        startActivity(Intent(this,OTPFirstStep::class.java))
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

    private companion object {
        const val REQ_ONE_TAP = 12345
    }
}