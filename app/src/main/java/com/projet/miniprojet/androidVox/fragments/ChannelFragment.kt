package com.projet.miniprojet.androidVox.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.databinding.FragmentChannelBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory


class ChannelFragment : Fragment() {
    private val args:ChannelFragmentArgs by navArgs()
    private var _binding: FragmentChannelBinding? = null
    private val binding get() = _binding!!

    private val client = ChatClient.instance()
    private lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelBinding.inflate(inflater, container, false)
        setupUser()
        setupChannels()
        return binding.root
    }



    private fun setupUser() {
        if(client.getCurrentUser()==null){
            user=if(args.chatUser.firstName.contains("Skander")){
                User(
                    id=args.chatUser.username,
                    extraData = mutableMapOf(
                        "name" to args.chatUser.firstName,
                        "country" to "Tunisia",
                        "image" to "https://lh3.googleusercontent.com/fife/AAWUweULVOa8Vs1NizspuL94tvNvlIrthwQUe7_GMJtlIrPQbFVNPsAhbIrpHZo0izmtDuhMrRsR_7rwj_DDCu-AOo47oJ4wgWEVZGP7abgilV6DmDoUyxrjwSLNM8OHJ1IpPg7uj0m-RviSXXGSStUzmGSnIVMSaI-AOW0W0rNqXrbH92UB3a-d1tOIjNmGiGbnlB5RTt898J3nUfvPuStpGlmQy6ZtImcNpCgtgNcEnTa-ZWqgKc74tNyJ7vEG6JPYljiZTxGWP-EN8o54dc-krFbVmSqVSCZMEE85zrYQz8IYbNN4apqeCjyilG08-zRAkMnhguNVV2H5MqpmyqU31laQOyCPEJp5D8fRKu-J33J1iLjtieUmdTzPAY82AxksB6Ksi7czjCK2W2YJtdwzycJqU6kGEumeqKaDdBdRiNlgJGRMoByzndyQBtvVOSBlEuT3wK2O-rLxRKEXhIw-wgdjY0AnRQb-NPawv505MfuGAosyVJFAeuGZDzJfLE7M8Qq06wUZelHAo_DahV8N5sAmVtXUTOxR2q325g8k0CEUGf8Cd7Dqd_DBTCT6kJPmVHNrmTospI0o0hBa25RerJ6INQpEe14Hd-XrfQ-J3vPTacH8lro7WPaMacnrR3zvSI2W9sXUYNsYXqjEscOfJo9nbH5WSgabCj4OnzfAFdA18G0QivSxQcWaNfVYdekn-3hW-QWssCf5c5AXepFQygY-Kln7QzonSPRSfbSPFOY-Jlk=b32"
                    )
                )
            }else{
                User(
                    id=args.chatUser.username,
                    extraData = mutableMapOf(
                        "name" to args.chatUser.firstName
                    )
                )
            }
            val token = client.devToken(user.id)
            client.connectUser(
                user,
                token
            ).enqueue{
                if(it.isSuccess){
                    Log.d("Channel Fragment", "setupUser: Success Connecting the user")
                }
                else{
                    Log.d("Channel Fragment ", it.error().message.toString())
                }
            }
        }
    }
    private fun setupChannels() {
        val filters = Filters.and(
            Filters.eq("type","messaging"),
            Filters.`in`("members", listOf(client.getCurrentUser()!!.id))
        )
        val viewModelFactory= ChannelListViewModelFactory(
            filters,
            ChannelListViewModel.DEFAULT_SORT
        )
        val listViewModel:ChannelListViewModel by viewModels { viewModelFactory }
        val listHeaderViewModel:ChannelListHeaderViewModel by viewModels()

        listHeaderViewModel.bindView(binding.channelListHeaderView,viewLifecycleOwner)
        listViewModel.bindView(binding.channelsView,viewLifecycleOwner)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}