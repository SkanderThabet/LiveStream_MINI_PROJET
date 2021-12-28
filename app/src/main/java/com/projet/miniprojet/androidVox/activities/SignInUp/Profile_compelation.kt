package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.datepicker.MaterialDatePicker
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import kotlinx.android.synthetic.main.activity_profile_compelation.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import com.projet.miniprojet.androidVox.activities.LiveStreamChatInteraction.MainActivity

import com.android.volley.VolleyError

import com.android.volley.toolbox.StringRequest

import com.android.volley.RequestQueue
import java.util.*
import kotlin.collections.HashMap


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

        val params: HashMap<String, String> = HashMap()
        params["email"] = outline_et_email.text.toString()
        params["passowrd"] = outline_et_password.text.toString()
        params["firstname"] = outline_et_firstname.text.toString()
        params["lastname"] = outline_et_lastname.text.toString()
        params["dob"] = tv_dob.text.toString()

        val apiKey = "https://voxappli.herokuapp.com/api/vox/auth/register"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            apiKey, JSONObject(params as Map<*, *>?),
            Response.Listener<JSONObject?> { response ->
                try {
                    if (response.getBoolean("success")) {
                        val token = response.getString("token")
                        //                            sharedPreferenceClass.setValue_string("token", token)
                        Toast.makeText(this@Profile_compelation, token, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Profile_compelation, HomePage::class.java))
                    }
                    progressBar_profile.isVisible=false
                } catch (e: JSONException) {
                    Log.e("Voll",e.stackTraceToString())
                    e.printStackTrace()
                    progressBar_profile.isVisible=false
                }
            }, Response.ErrorListener { error ->
                val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            Charset.forName(HttpHeaderParser.parseCharset(response.headers, "utf-8"))
                        )
                        val obj = JSONObject(res)
                        Toast.makeText(
                            this@Profile_compelation,
                            obj.getString("msg"),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("Voll",error.stackTraceToString())
                        Log.d("Voll",
                            "onErrorResponse: statusCode=" + error.networkResponse.statusCode
                        )
                        for (header in error.networkResponse.allHeaders) {
                            Log.d("Voll",
                                "onErrorResponse: headers: " + header.name.toString() + "=" + header.value
                            )
                        }
                        Log.i("jsonObjectRequest", "Error, Status Code " + error.networkResponse.statusCode);
                        Log.i("jsonObjectRequest", "URL: " + apiKey);
                        Log.i("jsonObjectRequest","Payload : "+JSONObject(params as Map<*, *>?).toString())
                        Log.i("jsonObjectRequest", "Net Response to String: " + error.networkResponse.toString());
                        Log.i("jsonObjectRequest", "Error bytes: " + String(error.networkResponse.data));
                        Log.e("Voll",error.stackTraceToString())
                        progressBar_profile.isVisible=false
                    } catch (je: JSONException) {
                        je.printStackTrace()
                        Log.e("Voll",je.stackTraceToString())
                        progressBar_profile.isVisible=false
                    } catch (je: UnsupportedEncodingException) {
                        je.printStackTrace()
                        Log.e("Voll",je.stackTraceToString())
                        progressBar_profile.isVisible=false
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                headers.put("User-agent", System.getProperty("http.agent"));
                Log.d("Voll",params.toString())
                return params
            }
        }

        // set retry policy

        // set retry policy
        val socketTime = 5000
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
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            tv_dob.text = "${calendar.get(Calendar.DAY_OF_MONTH)}/"+
            "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
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