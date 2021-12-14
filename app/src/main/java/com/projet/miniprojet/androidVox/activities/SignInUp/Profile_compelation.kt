package com.projet.miniprojet.androidVox.activities.SignInUp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.voxApp
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_profile_compelation.*
import org.bson.Document

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
                insertData()
            }
        }

    }

    private fun insertData() {
        val user: User? = voxApp.currentUser()
        val mongoClient = user?.getMongoClient("mongodb-atlas")
        val mongoDatabase = mongoClient?.getDatabase("User")
        val mongoCollection = mongoDatabase?.getCollection("users")
        mongoCollection?.insertOne(
            Document("userid", user.id).append(
                "username",
                et_username.editText.toString()
            ).append("firstname", et_firstname.editText.toString())
                .append("lastname", et_lastname.editText.toString())
                .append("dob", tv_dob.text.toString())

        )?.getAsync {
           if(it.isSuccess){
               Log.v("Data","Data inserted successfully")
           }
            else {
                Log.v("Data", "Error : "+it.error.toString())
           }
        }

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
        outline_et_username.error = null
        outline_et_lastname.error = null

        if (et_username.editText!!.text!!.isEmpty()) {
            outline_et_username.error = "Must not be empty"
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

        return true
    }


}