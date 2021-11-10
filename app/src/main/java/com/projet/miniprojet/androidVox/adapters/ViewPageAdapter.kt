package com.projet.miniprojet.androidVox.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import com.projet.miniprojet.R

class ViewPageAdapter(val context: Context) : PagerAdapter() {

    var layoutInflater: LayoutInflater? = null

    /**
     * Arrays of image
     * Head title
     * description
     */

    val imgArray = arrayOf(
        R.drawable.saving_1,
        R.drawable.safety_box_1,
        R.drawable.undraw_podcast_audience_re_4i5q_1
    )

    val headArray = arrayOf(
        "Start streaming and invite your friends",
        "You can join rooms and daily events",
        "Enjoy listening to our suggested podcasts"
    )
    val description = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint."

    override fun getCount(): Int {
        return headArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.slider, container, false)
        val img = view.findViewById<ImageView>(R.id.obt_img)
        val txtHead = view.findViewById<TextView>(R.id.text_head)
        val txtDescr = view.findViewById<TextView>(R.id.description)


        img.setImageResource(imgArray[position])
        txtHead.text = headArray[position]
        txtDescr.text = description


        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}