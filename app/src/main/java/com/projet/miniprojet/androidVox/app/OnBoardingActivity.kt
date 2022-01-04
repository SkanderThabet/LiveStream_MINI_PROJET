package com.projet.miniprojet.androidVox.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.SignInUp.oAuths
import com.projet.miniprojet.androidVox.adapters.ViewPageAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import java.lang.Boolean

class OnBoardingActivity : AppCompatActivity() {
    private val prevStarted="yes"
    lateinit var viewPager: ViewPager
    lateinit var viewPageAdapter: ViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        val nextbtn = findViewById<Button>(R.id.nextbtn)
        val getstartBtn = findViewById<Button>(R.id.getstartedbtn)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        viewPager = findViewById(R.id.viewpager)
        viewPageAdapter = ViewPageAdapter(this)
        viewPager.adapter = viewPageAdapter
        dotsIndicator.setViewPager(viewPager)
        nextButton()

        Log.i("Tag", viewPager.currentItem.toString())

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    nextbtn.visibility = View.INVISIBLE
                    getstartBtn.visibility = View.VISIBLE
                    getStartButton()

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }


        })
    }

    override fun onResume() {
        super.onResume()
        val sh:SharedPreferences =getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        if (!sh.getBoolean(prevStarted, false)) {
            val editor: SharedPreferences.Editor = sh.edit()
            editor.putBoolean(prevStarted, Boolean.TRUE)
            editor.apply()
        } else {
            openfirstact()
        }
    }
    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }

    private fun getStartButton() {
        val getstartBtn = findViewById<Button>(R.id.getstartedbtn)
        getstartBtn.setOnClickListener {
            openfirstact()
        }
    }

    private fun nextButton() {
        val nextbtn = findViewById<Button>(R.id.nextbtn)
        val getstartBtn = findViewById<Button>(R.id.getstartedbtn)
        nextbtn.setOnClickListener {
            viewPager.setCurrentItem(getItem(+1), true)
            if (viewPager.currentItem == 2) {
                nextbtn.visibility = View.INVISIBLE
                getstartBtn.visibility = View.VISIBLE
            }
            getStartButton()
        }
    }

    private fun openfirstact() {
        startActivity(Intent(this, oAuths::class.java))
        finish()
    }


}