package com.projet.miniprojet.androidVox.activities.OTP

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hbb20.CountryCodePicker
import com.projet.miniprojet.androidVox.R

import android.widget.TextView
import com.hbb20.CountryCodePicker.CustomDialogTextProvider
import kotlinx.android.synthetic.main.activity_otp2.*


class OTPFirstStep : AppCompatActivity() {
    lateinit var phoneNumber: String
    lateinit var countryCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Miniprojet)
        setContentView(R.layout.activity_otp2)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        val phonenumberET = findViewById<EditText>(R.id.phonenumberET)
        val getOTPbtn = findViewById<Button>(R.id.getOTPbtn)

        phonenumberET.addTextChangedListener {
            getOTPbtn.isEnabled = !(it.isNullOrEmpty() || it.length < 8)
        }

        getOTPbtn.setOnClickListener {
            checkNumber()
        }
        cpp.setCustomDialogTextProvider(object : CustomDialogTextProvider {
            override fun getCCPDialogTitle(
                language: CountryCodePicker.Language,
                defaultTitle: String
            ): String {
                return when (language) {
                    CountryCodePicker.Language.ENGLISH -> "Select Country/Region"
                    else -> defaultTitle
                }
            }

            override fun getCCPDialogSearchHintText(
                language: CountryCodePicker.Language?,
                defaultSearchHintText: String?
            ): String? {
                return defaultSearchHintText
            }

            override fun getCCPDialogNoResultACK(
                language: CountryCodePicker.Language,
                defaultNoResultACK: String
            ): String {
                return defaultNoResultACK
            }
        })
    }

    private fun checkNumber() {
        val ccp = findViewById<CountryCodePicker>(R.id.cpp)
        val phonenumberET = findViewById<EditText>(R.id.phonenumberET)


        countryCode = ccp.selectedCountryCodeWithPlus
        phoneNumber = countryCode + phonenumberET.text.toString()

        notifyUser()
    }

    private fun notifyUser() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage("We will be verifying the phone number : $phoneNumber\n" + "Is this OK, or would you like to edit the number ?")
            setPositiveButton("OK") { _, _ ->
                showOtpActivity()
            }
            setNegativeButton("Edit") { dialog, which ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    private fun showOtpActivity() {
        startActivity(Intent(this,OTPSecondStep::class.java).putExtra(PHONE_NUMBER,phoneNumber))
        finish()
    }
}