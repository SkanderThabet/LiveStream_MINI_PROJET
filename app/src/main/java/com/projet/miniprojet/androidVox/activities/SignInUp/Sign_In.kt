package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import kotlinx.android.synthetic.main.activity_profile_compelation.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class Sign_In : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        SignInButton.setOnClickListener {
            if (validate()) {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        progressBar_login.isVisible=true

        val params: HashMap<String, String> = HashMap()
        params["email"] = textinputEmail.text.toString()
        params["password"] = textinputpass.text.toString()

        val apiKey = "https://voxappli.herokuapp.com/api/vox/auth/login"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            apiKey, JSONObject(params as Map<*, *>?),
            Response.Listener<JSONObject?> { response ->
                try {
                    if (response.getBoolean("success")) {
                        val token = response.getString("token")
                        //                            sharedPreferenceClass.setValue_string("token", token)
                        Toast.makeText(this@Sign_In, token, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Sign_In, HomePage::class.java))
                    }
                    progressBar_login.isVisible=false
                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressBar_login.isVisible=false
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
                            this@Sign_In,
                            obj.getString("msg"),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar_login.isVisible=false
                    } catch (je: JSONException) {
                        je.printStackTrace()
                        progressBar_login.isVisible=false
                    } catch (je: UnsupportedEncodingException) {
                        je.printStackTrace()
                        progressBar_login.isVisible=false
                    }
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: HashMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json"
                return params
            }
        }

        // set retry policy

        // set retry policy
        val socketTime = 3000
        val policy: RetryPolicy = DefaultRetryPolicy(
            socketTime,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonObjectRequest.retryPolicy = policy

        // request add

        // request add
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }


    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }


//    private fun validateCredentials(): Boolean = when {
//        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
//        textinputEmail.text.toString().isEmpty() -> false
//        textinputpass.text.toString().isEmpty() -> false
//        else -> true
//    }

    private fun validate(): Boolean {
        textinputpass.error = null
        textinputEmail.error = null

        if (password_et.editText!!.text!!.isEmpty()) {
            textinputpass.error = "Must not be empty"
            password_et.error = "Must not be empty"
            return false
        }

        if (username_et.editText!!.text!!.isEmpty()) {
            textinputEmail.error = "Must not be empty"
            username_et.error = "Must not be empty"

            return false
        }

        return true
    }


}