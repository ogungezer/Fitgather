package com.example.fitgether.presentation.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitgether.R


class MainScreen : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val pref = activity?.getSharedPreferences("com.example.fitgether", Context.MODE_PRIVATE)
        val getPref = pref?.getBoolean("onboarding", false)

        if(!getPref!!){
            findNavController().navigate(MainScreenDirections.actionMainScreenToOnboardingFragment())

        } else {
            findNavController().navigate(MainScreenDirections.actionMainScreenToLoginFragment())
        }

        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }


}