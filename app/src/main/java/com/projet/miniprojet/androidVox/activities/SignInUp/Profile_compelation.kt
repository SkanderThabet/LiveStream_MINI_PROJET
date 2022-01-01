package com.projet.miniprojet.androidVox.activities.SignInUp

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.models.User
import com.projet.miniprojet.androidVox.other.SharedPref
import com.projet.miniprojet.androidVox.retrofit.ApiInterface
import com.projet.miniprojet.androidVox.retrofit.RetrofitInstance
import com.squareup.okhttp.ResponseBody
import kotlinx.android.synthetic.main.activity_profile_compelation.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.HashMap


class Profile_compelation : AppCompatActivity() {

    lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_compelation)

        sharedPref= SharedPref(this)
        tv_dob.setOnClickListener {
            datedob()
        }
        btn_complete_profile.setOnClickListener {
            if (validate()) {
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                signup()
//                registerUser()

            }
        }

    }


    private fun signup(){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val email=outline_et_email.text.toString()
        val password=outline_et_password.text.toString()
        val firstname=outline_et_firstname.text.toString()
        val lastname=outline_et_lastname.text.toString()
        val dob=tv_dob.text.toString()
        val registerInfo = User(email,password,firstname, lastname, dob)

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Retrofit",t.localizedMessage)
                Log.e("RetrofitError",t.stackTraceToString())
                Toast.makeText(
                    this@Profile_compelation,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(
                call: Call<User>,
                response: retrofit2.Response<User>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(this@Profile_compelation, "Success", Toast.LENGTH_SHORT)
                        .show()
                loginUser(email,password)
                }
                else{
                    Toast.makeText(this@Profile_compelation, "Registration failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }


    private fun loginUser(email:String,password:String) {
        progressBar_profile.isVisible=true

        val params: HashMap<String, String> = HashMap()
        params["email"] = email
        params["password"] = password

        val apiKey = "https://voxappli.herokuapp.com/api/vox/auth/login"

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            apiKey, JSONObject(params as Map<*, *>?),
            Response.Listener<JSONObject?> { response ->
                try {
                    if (response.getBoolean("success")) {
                        val token = response.getString("token")
                        sharedPref.setValue_string("token", token)
                        Toast.makeText(this@Profile_compelation, token, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Profile_compelation, HomePage::class.java))
                    }
                    progressBar_profile.isVisible=false
                } catch (e: JSONException) {
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
                        progressBar_profile.isVisible=false
                    } catch (je: JSONException) {
                        je.printStackTrace()
                        progressBar_profile.isVisible=false
                    } catch (je: UnsupportedEncodingException) {
                        je.printStackTrace()
                        progressBar_profile.isVisible=false
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

    override fun onStart() {
        super.onStart()
        val voxPref: SharedPreferences = getSharedPreferences("vox_app", MODE_PRIVATE)
        if(voxPref.contains("token")){
            startActivity(Intent(this@Profile_compelation,HomePage::class.java))
            finish()
        }
    }

}