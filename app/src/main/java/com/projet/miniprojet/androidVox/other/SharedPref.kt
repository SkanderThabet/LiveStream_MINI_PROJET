package com.projet.miniprojet.androidVox.other

import android.app.Activity
import android.content.Context

import android.content.SharedPreferences




class SharedPref(context: Context) {

    private val USER_PREF = "vox_app"
    private var appShared: SharedPreferences? = null
    private var prefsEditor: SharedPreferences.Editor? = null

    init {
        appShared = context.getSharedPreferences(USER_PREF, Activity.MODE_PRIVATE)
        prefsEditor = appShared!!.edit()
    }

    // int
    fun getValue_int(key: String?): Int {
        return appShared!!.getInt(key, 0)
    }

    fun setValue_int(key: String?, value: Int) {
        prefsEditor!!.putInt(key, value).commit()
    }

    // string
    fun getValue_string(key: String?): String? {
        return appShared!!.getString(key, "")
    }

    fun setValue_string(key: String?, value: String?) {
        prefsEditor!!.putString(key, value).commit()
    }


    // boolean
    fun getValue_boolean(key: String?): Boolean {
        return appShared!!.getBoolean(key, false)
    }

    fun setValue_boolean(key: String?, value: Boolean) {
        prefsEditor!!.putBoolean(key, value).commit()
    }

    fun clear() {
        prefsEditor!!.clear().commit()
    }
}