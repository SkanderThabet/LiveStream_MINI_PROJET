package com.projet.miniprojet.androidVox.activities.OTP

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.projet.miniprojet.R
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern

const val PHONE_NUMBER = "PhoneNumber"

class OTPSecondStep : AppCompatActivity() {
    var phoneNumber: String? = null
    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver: OTPBroadCastReceiver? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp3)
        val verifbtn=findViewById<Button>(R.id.verifybutton)
        val otpdigits= findViewById<EditText>(R.id.receivedOtpET)
        initViews()
        showTimer(60000)
        startSmartUserConsent()
        otpdigits.addTextChangedListener {
            verifbtn.isEnabled=!(it.isNullOrEmpty() || it.length < 6)
        }
    }

    private fun startSmartUserConsent() {
        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpMessage(message)
            }
        }
    }

    private fun getOtpMessage(message: String?) {
        val firstdigit = findViewById<TextView>(R.id.receivedOtpET)
        val otpPattern = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPattern.matcher(message)
        if (matcher.find()) {
            firstdigit!!.setText(matcher.group(0))
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = OTPBroadCastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener =
            object : OTPBroadCastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    startActivityForResult(intent, REQ_USER_CONSENT)

                }

                override fun onFailure() {

                }

            }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }


    private fun showTimer(milliSecInFuture: Long) {
        val timercounter = findViewById<TextView>(R.id.timer)
        val resendotp = findViewById<TextView>(R.id.resendnotrecieved)
        resendotp.isEnabled = false
        object : CountDownTimer(milliSecInFuture, 1000) {
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
        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        verifymessage.text = getString(R.string.enter_otp_sent, phoneNumber)
        setSpannableString()

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
                Toast.makeText(
                    applicationContext,
                    "OTP has been resend successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        span.setSpan(clickableSpan, span.length - 10, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        resendotp.movementMethod = LinkMovementMethod.getInstance()
        resendotp.text = span

    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }
}