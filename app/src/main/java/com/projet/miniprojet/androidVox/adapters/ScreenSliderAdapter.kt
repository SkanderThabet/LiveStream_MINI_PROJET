package com.projet.miniprojet.androidVox.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.projet.miniprojet.androidVox.fragments.InboxFragment
import com.projet.miniprojet.androidVox.fragments.PeopleFragment

class ScreenSliderAdapter(frag: FragmentActivity) : FragmentStateAdapter(frag) {
    override fun getItemCount(): Int = 2


    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> InboxFragment()
        else -> PeopleFragment()
    }

}
