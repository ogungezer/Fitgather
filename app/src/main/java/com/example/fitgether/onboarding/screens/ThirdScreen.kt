package com.example.fitgether.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentSecondScreenBinding
import com.example.fitgether.databinding.FragmentThirdScreenBinding


class ThirdScreen : Fragment() {

    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdScreenBinding.inflate(inflater,container,false)

        binding.screenThreeLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            setOnboardingDone()
        }


        return binding.root
    }



    private fun setOnboardingDone(){
        val pref = activity?.getSharedPreferences("com.example.fitgether", Context.MODE_PRIVATE) ?: return
        pref.edit().putBoolean("onboarding", true).apply()
    }






    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}