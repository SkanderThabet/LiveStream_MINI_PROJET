package com.projet.miniprojet.androidVox.activities.OTP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.projet.miniprojet.R
import java.util.concurrent.CompletableFuture

const val PHONE_NUMBER = "PhoneNumber"

class OTPSecondStep : AppCompatActivity() {
    var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp3)
        initViews()
        showTimer(60000)
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
                if(!resendotp.isEnabled){
                    ds.color=getColor(R.color.bluegray_400)
                }
                else{
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
}