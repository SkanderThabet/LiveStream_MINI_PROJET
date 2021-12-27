package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.datepicker.MaterialDatePicker
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import kotlinx.android.synthetic.main.activity_profile_compelation.*
import okio.Utf8
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class Profile_compelation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_compelation)


        tv_dob.setOnClickListener {
            datedob()
        }
        btn_complete_profile.setOnClickListener {
            if (validate()) {
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                registerUser()

            }
        }

    }

    private fun registerUser() {
        VolleyLog.DEBUG=true
        progressBar_profile.isVisible = true
        val params:MutableMap<String,String> = HashMap()
        params["email"] = et_email.editText!!.text.toString()
        params["passowrd"] = et_password.editText!!.text.toString()
        params["firstname"] = et_firstname.editText!!.text.toString()
        params["lastname"] = et_lastname.editText!!.text.toString()
        params["dob"] = tv_dob.text.toString()


        val apiKey = "https://voxappli.herokuapp.com/api/vox/auth/register"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            apiKey, JSONObject(params as Map<*, *>?), Response.Listener { response ->
                try {
                    if (response.getBoolean("success")) {
                        val token = response.getString("token")
//                        sharedPreferenceClass.setValue_string("token", token)
                        Toast.makeText(this, token, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomePage::class.java))
                    }
                    progressBar_profile.isVisible = false
                } catch (e: JSONException) {
                    Log.d("params",params.toString())
                    VolleyLog.v("VolleyTAG",e.localizedMessage)
                    e.printStackTrace()
                    progressBar_profile.isVisible = false
                }
            }, Response.ErrorListener { error ->
                val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {

                        val res = String(
                            response.data,
                            Charset.forName(HttpHeaderParser.parseCharset(response.headers,"UTF-8"))
                        )
                        val obj = JSONObject(res)
                        Toast.makeText(
                            this,
                            obj.getString("msg"),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar_profile.isVisible = false
                    } catch (je: JSONException ) {
                        VolleyLog.v("VolleyTAG",je.message)
                        je.printStackTrace()
                        progressBar_profile.isVisible = false
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                Log.d("params",params.toString())
                return params
            }
        }
        // set retry policy

        val socketTime = 3000
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTime,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.retryPolicy = policy

        // request add
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }


    private fun datedob() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(supportFragmentManager, "tag");
        datePicker.addOnPositiveButtonClickListener {
            tv_dob.text = ("Selected date : " + datePicker.headerText)
        }
        datePicker.addOnNegativeButtonClickListener {
            // Respond to negative button click.
        }
        datePicker.addOnCancelListener {
            // Respond to cancel button click.
        }
        datePicker.addOnDismissListener {
            // Respond to dismiss events.
        }
    }

    private fun validate(): Boolean {
        outline_et_firstname.error = null
        outline_et_email.error = null
        outline_et_lastname.error = null
        outline_et_password.error = null

        if (et_email.editText!!.text!!.isEmpty()) {
            outline_et_email.error = "Must not be empty"
            et_firstname.error = "Must not be empty"
            return false
        }

        if (et_firstname.editText!!.text!!.isEmpty()) {
            outline_et_firstname.error = "Must not be empty"
            return false
        }
        if (et_lastname.editText!!.text!!.isEmpty()) {
            outline_et_lastname.error = "Must not be empty"
            return false
        }
        if (et_password.editText!!.text!!.isEmpty()) {
            outline_et_password.error = "Must not be empty"
            return false
        }

        return true
    }


}