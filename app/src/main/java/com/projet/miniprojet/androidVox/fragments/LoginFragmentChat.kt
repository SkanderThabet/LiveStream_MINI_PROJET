package com.projet.miniprojet.androidVox.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.databinding.FragmentLoginChatBinding
import com.projet.miniprojet.androidVox.models.ChatUser


class LoginFragmentChat : Fragment() {

    private var _binding:FragmentLoginChatBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentLoginChatBinding.inflate(inflater,container,false)
        binding.button.setOnClickListener {
            authenticateUser()
        }
        return binding.root
    }

    private fun authenticateUser() {
        val firstName=binding.firstNameEditText.text.toString()
        val username=binding.usernameEditText.text.toString()
        if(validateInput(firstName,binding.firstNameInputLayout) && (validateInput(username,binding.usernameInputLayout))){
            val chatUser= ChatUser(firstName,username)
            val action = LoginFragmentChatDirections.actionLoginFragmentChatToChannelFragment(chatUser)
            findNavController().navigate(action)
        }

    }

    private fun validateInput(inputText:String,textInputLayout: TextInputLayout ) : Boolean{
        return if(inputText.length<=3){
            textInputLayout.isErrorEnabled=true
            textInputLayout.error="* Minimum 4 Characters Allowed"
            false
        }else{
            textInputLayout.isErrorEnabled=false
            textInputLayout.error=null
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null

    }


}