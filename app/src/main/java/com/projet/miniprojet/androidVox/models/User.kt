package com.projet.miniprojet.androidVox.models

import io.realm.RealmObject

open class User(
    val name: String,
    val firstname: String,
    val status: String,
    val picture: Int
) : RealmObject()