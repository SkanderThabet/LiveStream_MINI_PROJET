package com.projet.miniprojet.androidVox.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projet.miniprojet.androidVox.R
import io.realm.Realm

const val APP_ID="vox-kpgyp"

class RealmDB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realm_db)


    }
}