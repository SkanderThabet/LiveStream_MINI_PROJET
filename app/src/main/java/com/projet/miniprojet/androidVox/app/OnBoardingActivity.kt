package com.projet.miniprojet.androidVox.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Button
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.projet.miniprojet.R
import com.projet.miniprojet.androidVox.activities.OTP.OTPFirstStep
import com.projet.miniprojet.androidVox.activities.OTP.OTPSecondStep
import com.projet.miniprojet.androidVox.activities.OTP.PHONE_NUMBER
import com.projet.miniprojet.androidVox.adapters.ViewPageAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class OnBoardingActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager
    lateinit var viewPageAdapter: ViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        val nextbtn=findViewById<Button>(R.id.nextbtn)
        val getstartBtn=findViewById<Button>(R.id.getstartedbtn)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        viewPager = findViewById(R.id.viewpager)
        viewPageAdapter = ViewPageAdapter(this)
        viewPager.adapter = viewPageAdapter
        dotsIndicator.setViewPager(viewPager)
        nextButton()

        Log.i("Tag",viewPager.currentItem.toString())


    }




    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }
    private fun nextButton() {
        val nextbtn=findViewById<Button>(R.id.nextbtn)
        val getstartBtn=findViewById<Button>(R.id.getstartedbtn)
        nextbtn.setOnClickListener {
            viewPager.setCurrentItem(getItem(+1),true)
            if(viewPager.currentItem==2){
                nextbtn.visibility=View.INVISIBLE
                getstartBtn.visibility=View.VISIBLE
            }
            getstartBtn.setOnClickListener {
                otpFirstAct()
            }
        }
    }

    private fun otpFirstAct() {
        startActivity(Intent(this, OTPFirstStep::class.java))
        finish()
    }


}