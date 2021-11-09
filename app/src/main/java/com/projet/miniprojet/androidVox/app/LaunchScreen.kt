package com.projet.miniprojet.androidVox.app

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.projet.miniprojet.R

class LaunchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)
        val logoname= findViewById<TextView>(R.id.LOGONAME)
        val paint = logoname.paint
        val width = paint.measureText(logoname.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, logoname.textSize, intArrayOf(
            Color.parseColor("#A44EF4"),
            Color.parseColor("#42A2E7"),

        ), null, Shader.TileMode.REPEAT)

        logoname.paint.shader = textShader
    }
}