package com.example.fitgether.presentation.view.createFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentLastCreateSectionBinding
import com.example.fitgether.presentation.viewmodel.CreateEventViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max

@AndroidEntryPoint
class LastCreateSection : Fragment() {

    private var _binding : FragmentLastCreateSectionBinding? = null
    private val binding get() = _binding!!
    private val updateStepViewmodel : UpdateStepViewmodel by activityViewModels()
    private val createEventViewmodel : CreateEventViewmodel by activityViewModels()
    private lateinit var auth: FirebaseAuth


    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.Genders)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_menu_item,categories)
        binding.genderCategoryCompleteText.setAdapter(arrayAdapter)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLastCreateSectionBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()


        binding.lastCreateButton.setOnClickListener {
            genderChoiceControl()

            val genderChoice = binding.genderCategoryCompleteText.text.toString()
            val maxPerson = binding.rangeSlider.value.toInt()

            if(binding.genderCategoryTextLayout.error == null && binding.createEventCheckbox.isChecked){
                createEventViewmodel.createEvent(genderChoice = genderChoice, maxPersonCount = maxPerson, eventCreatorId = auth.currentUser?.uid)
                createEventViewmodel.pushCreatedEventToDb()
                Toast.makeText(requireContext(),"etkinlik oluşturuldu!", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed() //geri döneceğiz ana ekrana
            }
        }


        binding.lastBackButton.setOnClickListener{
            updateStepViewmodel.updateStep(1)
        }


        return binding.root

    }



    private fun genderChoiceControl() {

        if(binding.genderCategoryCompleteText.text.toString().isEmpty()){
            binding.genderCategoryTextLayout.error = "Lütfen cinsiyet tercihi yapın."
        }

        binding.genderCategoryCompleteText.setOnItemClickListener { parent, view, position, id ->
            binding.genderCategoryTextLayout.error = null
        }
    }


}