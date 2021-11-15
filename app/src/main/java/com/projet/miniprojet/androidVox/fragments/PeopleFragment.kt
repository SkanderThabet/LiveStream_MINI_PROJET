package com.projet.miniprojet.androidVox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.viewHolder.UserViewHolder
import kotlinx.android.synthetic.main.fragment_chats.*

class PeopleFragment : Fragment() {

    lateinit var mAdapter:UserViewHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_chat.apply{
            layoutManager=LinearLayoutManager(requireContext())

        }
    }
}
