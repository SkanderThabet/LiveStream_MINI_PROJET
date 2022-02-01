package com.projet.miniprojet.androidVox.activities.SignInUp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
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
    val storage by lazy {
        FirebaseStorage.getInstance()
    }
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    lateinit var downloadURL:String
    lateinit var sharedPref: SharedPref
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_compelation)

        sharedPref = SharedPref(this)
        tv_dob.setOnClickListener {
            datedob()
        }
        profile_image.setOnClickListener {
            checkForPermission()
        }
        btn_complete_profile.setOnClickListener {
            val email = outline_et_email.text.toString()
            val password = outline_et_password.text.toString()
            val firstname = outline_et_firstname.text.toString()
            val lastname = outline_et_lastname.text.toString()
            val dob = tv_dob.text.toString()
            if (!::downloadURL.isInitialized) {
                Toast.makeText(this,"Photo cannot be empty",Toast.LENGTH_SHORT).show()
            }
            if (isValidForm(email, password, firstname, lastname, dob)) {
//                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                signup()

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkForPermission() {
        if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) && (checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, 1001)
            requestPermissions(permissionWrite, 1002)
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode==1000){
            data?.data?.let {
                profile_image.setImageURI(it)
                uploadImage(it)
            }
        }
    }

    private fun uploadImage(it: Uri) {
        btn_complete_profile.isEnabled=false
        val ref = storage.reference.child("uploads/"+auth.uid.toString())
        val uploadTask = ref.putFile(it)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,Task<Uri>>{ task ->
            if(!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener {task ->
            btn_complete_profile.isEnabled=true
            if(task.isSuccessful){
                downloadURL=task.result.toString()
                Log.i("URL","downloadUrl : $downloadURL")
            }
            else{

            }
        }.addOnFailureListener {

        }
    }


    private fun signup() {
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val email = outline_et_email.text.toString()
        val password = outline_et_password.text.toString()
        val firstname = outline_et_firstname.text.toString()
        val lastname = outline_et_lastname.text.toString()
        val dob = tv_dob.text.toString()
        val registerInfo = User(email, password, firstname, lastname, dob, avatar = downloadURL)

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Retrofit", t.localizedMessage)
                Log.e("RetrofitError", t.stackTraceToString())
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
//                    Toast.makeText(this@Profile_compelation, "Success", Toast.LENGTH_SHORT)
//                        .show()
                    loginUser(email, password)
                } else {
                    Toast.makeText(
                        this@Profile_compelation,
                        "Registration failed!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })
    }


    private fun loginUser(email: String, password: String) {
        progressBar_profile.isVisible = true

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
//                        Toast.makeText(this@Profile_compelation, token, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Profile_compelation, HomePage::class.java))
                    }
                    progressBar_profile.isVisible = false
                } catch (e: JSONException) {
                    e.printStackTrace()
                    progressBar_profile.isVisible = false
                }
            }, Response.ErrorListener { error ->
                val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            Charset.forName(
                                HttpHeaderParser.parseCharset(
                                    response.headers,
                                    "utf-8"
                                )
                            )
                        )
                        val obj = JSONObject(res)
                        Toast.makeText(
                            this@Profile_compelation,
                            obj.getString("msg"),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar_profile.isVisible = false
                    } catch (je: JSONException) {
                        je.printStackTrace()
                        progressBar_profile.isVisible = false
                    } catch (je: UnsupportedEncodingException) {
                        je.printStackTrace()
                        progressBar_profile.isVisible = false
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
            tv_dob.text = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
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
    private fun isValidForm(email: String, password: String,firstname:String,lastname:String,dob:String): Boolean {
        var isValid = true

        if (firstname.isEmpty()){
            et_firstname.isErrorEnabled = true
            et_firstname.error = "Name cannot be empty!"
            isValid = false
        }else{
            outline_et_firstname.doOnTextChanged { text, start, before, count ->
                et_firstname.isErrorEnabled = false
                outline_et_firstname.error=null
            }
            et_firstname.isErrorEnabled = false
        }
        if (lastname.isEmpty()){
            et_lastname.isErrorEnabled = true
            et_lastname.error = "Surname cannot be empty!"
            isValid = false
        }else{
            et_lastname.isErrorEnabled = false
        }
        if (!email.isValidEmail()){
            et_email.isErrorEnabled = true
            et_email.error = "Email address is wrong!"
            isValid = false
        }else{
            et_email.isErrorEnabled = false
        }
        if (password.isEmpty()){
            et_password.isErrorEnabled = true
            et_password.error = "Password cannot be empty!"
            isValid = false
        }
        else if(password.length < 8){
            et_password.isErrorEnabled = true
            et_password.error = "Password length must be longer than 8."
            isValid = false
        }
        else{
            et_password.isErrorEnabled = false
        }
        if (dob.isEmpty()){
            tv_dob.error = "You should pick a date it's required!"
            isValid=false
        }
        else{
            tv_dob.error=null
        }
        return isValid
    }
    private fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()


    override fun onStart() {
        super.onStart()
        val voxPref: SharedPreferences = getSharedPreferences("vox_app", MODE_PRIVATE)
        if (voxPref.contains("token")) {
            startActivity(Intent(this@Profile_compelation, HomePage::class.java))
            finish()
        }
    }

}