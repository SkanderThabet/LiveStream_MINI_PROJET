package com.projet.miniprojet.androidVox.activities.OTP

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Chat.ChatMain
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.activities.SignInUp.Sign_Up
import java.util.concurrent.TimeUnit

const val PHONE_NUMBER = "PhoneNumber"

class OTPSecondStep : AppCompatActivity(), View.OnClickListener {
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var progressDialog: ProgressDialog
    var phoneNumber: String? = null
    var mVerificationID: String? = null
    var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var mCounterDown: CountDownTimer? = null

    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: OTPBroadCastReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp3)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        val verifbtn = findViewById<Button>(R.id.verifybutton)
        val otpdigits = findViewById<EditText>(R.id.receivedOtpET)
        initViews()
        startVerify()
//        startSmartUserConsent()
        otpdigits.addTextChangedListener {
            verifbtn.isEnabled = !(it.isNullOrEmpty() || it.length < 6)
        }
    }

    private fun startVerify() {
        val auth = FirebaseAuth.getInstance()
//        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true);
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        showTimer(60000)
        progressDialog = createProgressDialog("Sending a verification code", false)
        progressDialog.show()

    }

//    private fun startSmartUserConsent() {
//        val client = SmsRetriever.getClient(this)
//        client.startSmsUserConsent(null)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQ_USER_CONSENT) {
//            if (resultCode == RESULT_OK && data != null) {
//                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
//                getOtpMessage(message)
//            }
//        }
//    }
//
//    private fun getOtpMessage(message: String?) {
//        val firstdigit = findViewById<TextView>(R.id.receivedOtpET)
//        val otpPattern = Pattern.compile("(|^)\\d{6}")
//        val matcher = otpPattern.matcher(message)
//        if (matcher.find()) {
//            firstdigit!!.setText(matcher.group(0))
//        }
//    }
//
//    private fun registerBroadcastReceiver() {
//        smsBroadcastReceiver = OTPBroadCastReceiver()
//        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
//            object : OTPBroadCastReceiver.SmsBroadcastReceiverListener {
//                override fun onSuccess(intent: Intent?) {
//                    startActivityForResult(intent, REQ_USER_CONSENT)
//
//                }
//
//                override fun onFailure() {
//
//                }
//
//            }
//        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
//        registerReceiver(smsBroadcastReceiver, intentFilter)
//    }


    private fun showTimer(milliSecInFuture: Long) {
        val timercounter = findViewById<TextView>(R.id.timer)
        val resendotp = findViewById<TextView>(R.id.resendnotrecieved)
        resendotp.isEnabled = false
        mCounterDown = object : CountDownTimer(milliSecInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timercounter.isVisible = true
                timercounter.text = getString(R.string.second_remaining, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                timercounter.isVisible = false
                resendotp.isEnabled = true

            }
        }.start()

    }

    private fun initViews() {
        val verifymessage = findViewById<TextView>(R.id.enter_otp_message)
        val firstdigit = findViewById<TextView>(R.id.receivedOtpET)
        val verifBtn = findViewById<Button>(R.id.verifybutton)
        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        verifymessage.text = getString(R.string.enter_otp_sent, phoneNumber)
        setSpannableString()

        verifBtn.setOnClickListener(this)




        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     UserFragment action.

                //check if progressdialog is initialized or not
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                val smsCode: String? = credential.smsCode
                if (!smsCode.isNullOrBlank())
                    firstdigit.setText(smsCode)

                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
                Log.e("ERROR-Firebase", e.localizedMessage)
                notifyUserAndRetry("Your phone number might be wrong or connection error.Retry again!")
            }

            override fun onCodeSent(

                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                val timercounter = findViewById<TextView>(R.id.timer)
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the UserFragment to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                timercounter.isVisible = false
                // Save verification ID and resending token so we can use them later
                mVerificationID = verificationId
                mResendToken = token
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
//        mAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }
                    //First Time Login
                    if (it.result?.additionalUserInfo?.isNewUser == true) {
                        startProfileSettingsActivity()
                    } else {
                        showLoginActivity()
                    }
                } else {

                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }

                    notifyUserAndRetry("Your Phone Number Verification is failed.Retry again!")
                }
            }

    }

    private fun startProfileSettingsActivity() {
        startActivity(Intent(this, HomePage::class.java))
        finish()
    }

    private fun notifyUserAndRetry(message: String) {
        MaterialAlertDialogBuilder(this).apply {
            setMessage(message)
            setPositiveButton("Ok") { _, _ ->
                showLoginActivity()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }

    }

    private fun showLoginActivity() {
        startActivity(Intent(this, Sign_Up::class.java))
        finish()
    }

    private fun setSpannableString() {
        val resendotp = findViewById<TextView>(R.id.resendnotrecieved)
        val span = SpannableString(getString(R.string.resend_otp_if_not_recieved))
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                if (!resendotp.isEnabled) {
                    ds.color = getColor(R.color.bluegray_400)
                } else {
                    ds.color = getColor(R.color.indigo_A700)
                }
            }

            override fun onClick(p0: View) {
                if (mResendToken != null) {
                    showTimer(60000)
                    progressDialog = createProgressDialog("Sending a verification code ", false)
                    progressDialog.show()
                    val auth = FirebaseAuth.getInstance()
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber!!)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this@OTPSecondStep)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(mResendToken!!)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
            }

        }
        span.setSpan(clickableSpan, span.length - 10, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        resendotp.movementMethod = LinkMovementMethod.getInstance()
        resendotp.text = span

    }

//    override fun onStart() {
//        super.onStart()
//        registerBroadcastReceiver()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(smsBroadcastReceiver)
//    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCounterDown != null) {
            mCounterDown!!.cancel()
        }
    }

    override fun onClick(p0: View?) {
        val verifbtn = findViewById<Button>(R.id.verifybutton)
        val firstdigit = findViewById<EditText>(R.id.receivedOtpET)
        when (p0) {
            verifbtn -> {
                val code = firstdigit.text.toString()
                if (code.isNotEmpty() && !mVerificationID.isNullOrEmpty()) {
                    progressDialog = createProgressDialog("Please wait...", false)
                    progressDialog.show()
                    val credential =
                        PhoneAuthProvider.getCredential(mVerificationID!!, code)
                    signInWithPhoneAuthCredential(credential)
                }
            }
        }
    }


}

fun Context.createProgressDialog(message: String, isCancelable: Boolean): ProgressDialog {
    return ProgressDialog(this).apply {
        setCancelable(false)
        setMessage(message)
        setCanceledOnTouchOutside(false)
    }
}


