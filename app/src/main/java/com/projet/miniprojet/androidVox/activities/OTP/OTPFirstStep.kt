package com.projet.miniprojet.androidVox.activities.OTP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hbb20.CountryCodePicker
import com.projet.miniprojet.R

class OTPFirstStep : AppCompatActivity() {
    lateinit var phoneNumber: String
    lateinit var countryCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp2)
        val phonenumberET = findViewById<EditText>(R.id.phonenumberET)
        val getOTPbtn = findViewById<Button>(R.id.getOTPbtn)

        phonenumberET.addTextChangedListener {
            getOTPbtn.isEnabled = !(it.isNullOrEmpty() || it.length < 8)
        }

        getOTPbtn.setOnClickListener {
            checkNumber()
        }
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