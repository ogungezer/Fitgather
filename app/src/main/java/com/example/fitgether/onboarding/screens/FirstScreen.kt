package com.example.fitgether.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentFirstScreenBinding


class FirstScreen : Fragment() {

    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstScreenBinding.inflate(inflater,container,false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.screenOneButton.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.skipText.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            setOnboardingDone()
        }

        return binding.root
    }


    private fun setOnboardingDone(){
        val pref = activity?.getSharedPreferences("com.example.fitgether", Context.MODE_PRIVATE) ?: return
        pref.edit().putBoolean("onboarding", true).apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}