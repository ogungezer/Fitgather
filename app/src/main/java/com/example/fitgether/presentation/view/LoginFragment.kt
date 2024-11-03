package com.example.fitgether.presentation.view

import android.app.Notification.Action
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fitgether.MainActivity
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentFirstScreenBinding
import com.example.fitgether.databinding.FragmentLoginBinding
import com.example.fitgether.presentation.viewmodel.LoginViewmodel
import com.example.fitgether.presentation.viewmodel.RegisterViewmodel
import com.example.fitgether.utils.AuthErrorType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewmodel by viewModels<LoginViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater,container,false )
        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.navBarColor)

        errorAuthObserver()

        mailControl()
        passwordControl()


        binding.loginTxtBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmailEditTxt.text.toString()
            val password = binding.loginPasswordEdtTxt.text.toString()
            if(binding.loginEmailInputLayout.error == null && binding.loginPasswordInputLayout.error == null){
                if(loginViewmodel.inputControl(email,password)){
                    loginViewmodel.loginUserAuth(email,password){ isLogged ->
                        if(isLogged){
                            val intent = Intent(requireContext(), HomeActivity::class.java)//maybe requıreContext -> context al
                            startActivity(intent)
                            activity?.finish()
                            //findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                        }else{
                            Log.d("login","login basarısız")
                        }
                    }
                }else{
                    Log.d("login", "incorrect inputs")
                }
            }

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

    private fun errorAuthObserver(){
        loginViewmodel.errorState.observe(viewLifecycleOwner, Observer { errorType ->
            val animationFadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_anim)
            val animationFadeOut= AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_anim)
            when(errorType){
                AuthErrorType.INCORRECT_LOGIN_INFORMATION -> {
                    binding.loginBtn.isClickable = false
                    binding.authErrorMsg.visibility = View.VISIBLE
                    binding.authErrorMsg.startAnimation(animationFadeIn)
                    binding.authErrorMsg.text = "Kullanıcı adı veya şifre yanlış!"
                    binding.authErrorMsg.setBackgroundResource(R.drawable.auth_error_message_bg)
                }
                AuthErrorType.SERVICE_ERROR -> {
                    binding.loginBtn.isClickable = false
                    binding.authErrorMsg.visibility = View.VISIBLE
                    binding.authErrorMsg.startAnimation(animationFadeIn)
                    binding.authErrorMsg.text = "Bir hata meydana geldi."
                    binding.authErrorMsg.setBackgroundResource(R.drawable.auth_error_message_bg)
                }
                AuthErrorType.NO_ERROR -> {
                    binding.loginBtn.isClickable = false
                    binding.authErrorMsg.visibility = View.VISIBLE
                    binding.authErrorMsg.startAnimation(animationFadeIn)
                    binding.authErrorMsg.text = "Giriş yapılıyor..."
                    binding.authErrorMsg.setBackgroundResource(R.drawable.auth_correct_message_bg)
                }
                else -> {
                    binding.loginBtn.isClickable = true
                    binding.authErrorMsg.visibility = View.GONE
                    binding.authErrorMsg.startAnimation(animationFadeOut)
                }
            }
        })
    }

}