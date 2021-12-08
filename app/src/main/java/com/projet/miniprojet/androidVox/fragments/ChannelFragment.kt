package com.projet.miniprojet.androidVox.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.activities.Chat.ChatMain
import com.projet.miniprojet.androidVox.activities.Homepage.HomePage
import com.projet.miniprojet.androidVox.databinding.FragmentChannelBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.chat.android.ui.avatar.AvatarView
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.header.ChannelListHeaderView
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import kotlinx.android.synthetic.main.fragment_channel.*
import kotlinx.android.synthetic.main.slider.view.*


class ChannelFragment : Fragment() {
    private val args: ChannelFragmentArgs by navArgs()

    private var _binding: FragmentChannelBinding? = null
    private val binding get() = _binding!!

    private val client = ChatClient.instance()
    private lateinit var user: User

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChannelBinding.inflate(inflater, container, false)


        setupUser()
        setupChannels()
        setupDrawer()

        binding.channelsView.setChannelDeleteClickListener { channel ->
            deleteChannel(channel)
        }

        binding.channelListHeaderView.setOnActionButtonClickListener {
            findNavController().navigate(R.id.action_channelFragment_to_usersFragment)
        }


        binding.channelsView.setChannelItemClickListener { channel ->
            val action = ChannelFragmentDirections.actionChannelFragmentToChatFragment(channel.cid)
            findNavController().navigate(action)
        }

        binding.channelListHeaderView.setOnUserAvatarClickListener {
            binding.drawerLayout.openDrawer(Gravity.START)
        }

        return binding.root
    }

    private fun setupUser() {
        if (client.getCurrentUser() == null) {
            user = if (args.chatUser.firstName.contains("SkanderThabet")) {
                User(
                    id = args.chatUser.username,
                    extraData = mutableMapOf(
                        "name" to args.chatUser.firstName,
                        "county" to "Serbia",
                        "image" to "https://lh3.googleusercontent.com/fife/AAWUweUhX40OYOIPBez2bjYEzUkzPMSu4DafwDdZOvrbv0WnN4CoHSRdhfmTILoci8QFgccB_6InDI4egmSLcs_y_p0bzRg9HjJbHggtbms9MaSeBEkcdbgS427Q-NjhHTUS6eSN0FIsm-7MV2_mXbKq4bGVlA32YHgS4VwOJa0G_bO5a0cTcD6Fxy96ZQUUduemtqvmVZ9UkpDqxdQdOjvrVLXPIyH0RYHk5iK5rqPEvGaIRQF5r6G284VH31R_OwfutzQcOm_ayQan5kODsmOi-iXb4TpGe4qKw_Cz--UgJxkrBBUOG9_D538vZXRMbhnDP2K01XEGCLJriPfP8rmrrMYbDVRXiN2VA1T0dbdFitMtrJNdGC0eJdF7Kb3Bj5zNc9E4m0VzFRfdvC1-bBRWngECZzMh2TgrTlYAQ8th8HKsq8FUMaOXP7VFgLn55kbfxUmycP6hKLlLxvoQ2r-hySZ1wmb54sGfCScx4dqSVm_GyYnJsF8Yx9IF2CM8_q7Iwq-9KdDSe9YBET0F8NmFnUekI0gDaRnO4GszBzWNDGqPBWgjtRmYeLrRvVFN53HrHIeZm6fTH677hqI8Vw0ykdSN08QehDRJAFetdGJnIqWsVS54xq4pKkbL1YzjxhNhQrQlOeH_rfWxIiG2vVFx6rOvC8mzq4frTJ6pBqAt5hWgU7UroKpcDrmgIIo4mVyvaqiKAoMTj2qVg5Cb_20GzzFZJQK0_PYW4vOB4muVEtw5Dxc=b"
                    )
                )
            } else {
                User(
                    id = args.chatUser.username,
                    extraData = mutableMapOf(
                        "name" to args.chatUser.firstName
                    )
                )
            }
            val token = client.devToken(user.id)
            client.connectUser(
                user = user,
                token = token
            ).enqueue { result ->
                if (result.isSuccess) {
                    Log.d("ChannelFragment", "Success Connecting the User")
                } else {
                    Log.d("ChannelFragment", result.error().message.toString())
                }
            }
        }
    }

    private fun setupChannels() {
        val filters = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf(client.getCurrentUser()!!.id))
        )
        val viewModelFactory = ChannelListViewModelFactory(
            filters,
            ChannelListViewModel.DEFAULT_SORT,

        )
        val listViewModel: ChannelListViewModel by viewModels { viewModelFactory }
        val listHeaderViewModel: ChannelListHeaderViewModel by viewModels()

        listHeaderViewModel.bindView(binding.channelListHeaderView, viewLifecycleOwner)
        listViewModel.bindView(binding.channelsView, viewLifecycleOwner)



    }

    private fun setupDrawer() {
        binding.signoutBtn.setOnClickListener {
            logout()
        }
        binding.navigationView.menu.getItem(1).isChecked = true

        binding.navigationView.setNavigationItemSelectedListener {
            if(it.itemId==R.id.home_menu){
                startHomeAct()
            }
            false
        }
        val currentUser = client.getCurrentUser()!!
        val headerView = binding.navigationView.getHeaderView(0)
        val headerAvatar = headerView.findViewById<AvatarView>(R.id.avatarView)
        headerAvatar.setUserData(currentUser)
        val headerId = headerView.findViewById<TextView>(R.id.id_textView)
        headerId.text = currentUser.id
        val headerName = headerView.findViewById<TextView>(R.id.name_textView)
        headerName.text = currentUser.name
    }

    private fun startHomeAct() {
        startActivity(Intent(this.requireContext(), HomePage::class.java))

    }

    private fun deleteChannel(channel: Channel) {
        ChatDomain.instance().deleteChannel(channel.cid).enqueue { result ->
            if (result.isSuccess) {
                showToast("Channel: ${channel.name} removed!")
            } else {
                Log.e("ChannelFragment", result.error().message.toString())
            }
        }
    }

    private fun logout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            client.disconnect()
            findNavController().navigate(R.id.action_channelFragment_to_loginFragment)
            showToast("Logged out successfully")
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Logout?")
        builder.setMessage("Are you sure you want to logout?")
        builder.create().show()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}