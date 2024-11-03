package com.example.fitgether.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.size
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentCreateEventBinding
import com.example.fitgether.databinding.FragmentFirstScreenBinding
import com.example.fitgether.presentation.view.createFragments.CreateEventPagerAdapter
import com.example.fitgether.presentation.view.createFragments.FirstCreateSection
import com.example.fitgether.presentation.view.createFragments.LastCreateSection
import com.example.fitgether.presentation.view.createFragments.SecondCreateSection
import com.example.fitgether.presentation.view.createFragments.UpdateStepViewmodel
import com.example.fitgether.presentation.viewmodel.CreateEventViewmodel
import com.example.fitgether.presentation.viewmodel.LoginViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEventFragment() : Fragment() {

    private var _binding: FragmentCreateEventBinding? = null
    private val binding get() = _binding!!
    private val updateStepViewmodel : UpdateStepViewmodel by activityViewModels()
    private val createEventViewmodel : CreateEventViewmodel by activityViewModels()

    private val progressBarDescriptions = mutableListOf("Temel Bilgiler", "Zaman ve Yer", "Katılımcı Bilgileri")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateEventBinding.inflate(inflater,container,false)

        val stepView = binding.stepView
        stepView.setSteps(progressBarDescriptions)


        val fragments = arrayOf(
            FirstCreateSection(),
            SecondCreateSection(),
            LastCreateSection()
        )

        val adapter = CreateEventPagerAdapter(fragments, requireActivity().supportFragmentManager , lifecycle)

        val viewPager = binding.createEventViewPager


        viewPager.adapter = adapter
        viewPager.setUserInputEnabled(false)

        updateStepViewmodel.currentStep.observe(viewLifecycleOwner) { step ->
            println("observe: $step")
            viewPager.currentItem = step
            stepView.go(step, true)
        }


        binding.backPressButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            createEventViewmodel.locationControl(false)
            updateStepViewmodel.updateStep(0)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                createEventViewmodel.locationControl(false)
                updateStepViewmodel.updateStep(0)
                if(isEnabled){ //activitydeki onbackpressed kullanması için
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })


        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}