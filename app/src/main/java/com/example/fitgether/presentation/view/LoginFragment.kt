package com.example.fitgether.presentation.view

import android.app.Notification.Action
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentFirstScreenBinding
import com.example.fitgether.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater,container,false )
        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.navBarColor)


        mailControl()
        passwordControl()

        binding.loginTxtBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mailControl() {
        binding.loginEmailEditTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(isEmailValid(text)){
                    binding.loginEmailInputLayout.error = null
                }else{
                    binding.loginEmailInputLayout.error = "Geçerli bir mail formatı giriniz."
                }
            }
        }
    }

    private fun isEmailValid(email : CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun passwordControl() {
        binding.loginPasswordEdtTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(text.length < 8){
                    binding.loginPasswordInputLayout.errorIconDrawable = null
                    binding.loginPasswordInputLayout.error = "Şifre en az 8 karakterden oluşmalı."
                }else if(isPasswordOnlyDigit(text.toString())){
                    binding.loginPasswordInputLayout.errorIconDrawable = null
                    binding.loginPasswordInputLayout.error = "Şifre sadece sayılardan oluşamaz."
                }else{
                    binding.loginPasswordInputLayout.errorIconDrawable = null
                    binding.loginPasswordInputLayout.error = null
                }
            }
        }
    }

    private fun isPasswordOnlyDigit(text : String): Boolean {
        for(char in text){
            if(!char.isDigit()){
                return false
            }
        }
        return true
    }

}