package com.example.fitgether.presentation.view.createFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.fitgether.MainActivity
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentCreateEventBinding
import com.example.fitgether.databinding.FragmentFirstCreateSectionBinding
import com.example.fitgether.databinding.FragmentLoginBinding
import com.example.fitgether.databinding.FragmentMainScreenBinding
import com.example.fitgether.presentation.viewmodel.CreateEventViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstCreateSection : Fragment() {

    private var _binding: FragmentFirstCreateSectionBinding? = null
    private val binding get() = _binding!!
    private val updateStepViewmodel : UpdateStepViewmodel by activityViewModels()
    private val createEventViewmodel : CreateEventViewmodel by activityViewModels()


    //Bug olmaması için onResume içerisine koyuldu.
    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.Categories)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_menu_item,categories)
        binding.categoriesCompleteText.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstCreateSectionBinding.inflate(inflater,container,false)


        binding.firstCreateButton.setOnClickListener {
            eventTitleControl()
            eventDescriptionControl()
            categoryControl()

            val category = binding.categoriesCompleteText.text.toString()
            val title = binding.eventTitleEditText.text.toString()
            val description = binding.eventDescriptionEditText.text.toString()

            if(binding.categoriesTextLayout.error == null &&
                binding.eventTitleTextLayout.error == null &&
                binding.eventDescriptionTextLayout.error == null
            ){
                createEventViewmodel.createEvent(category = category, title = title, description = description )
                updateStepViewmodel.updateStep(1)
            }

        }

        return binding.root
    }

    private fun categoryControl() {

        if(binding.categoriesCompleteText.text.toString().isEmpty()){
            binding.categoriesTextLayout.error = "Lütfen bir kategori seçin"
        }

        binding.categoriesCompleteText.setOnItemClickListener { parent, view, position, id ->
            binding.categoriesTextLayout.error = null
        }

    }

    private fun eventTitleControl(){

        binding.eventTitleEditText.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                binding.eventTitleTextLayout.error = "Bu alan boş bırakılamaz."
            }else{
                binding.eventTitleTextLayout.error = null
            }
        }

        if(binding.eventTitleEditText.text.isNullOrBlank()){
            binding.eventTitleTextLayout.error = "Bu alan boş bırakılamaz"
        }

    }

    private fun eventDescriptionControl(){

        binding.eventDescriptionEditText.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrBlank()){
                binding.eventDescriptionTextLayout.error = "Bu alan boş bırakılamaz."
            }else{
                binding.eventDescriptionTextLayout.error = null
            }
        }

        if(binding.eventDescriptionEditText.text.isNullOrBlank()){
            binding.eventDescriptionTextLayout.error = "Bu alan boş bırakılamaz"
        }

    }


}