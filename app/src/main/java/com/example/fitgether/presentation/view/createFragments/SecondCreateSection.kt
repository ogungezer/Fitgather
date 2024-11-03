package com.example.fitgether.presentation.view.createFragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentFirstCreateSectionBinding
import com.example.fitgether.databinding.FragmentSecondCreateSectionBinding
import com.example.fitgether.presentation.view.CreateEventFragment
import com.example.fitgether.presentation.view.MapsFragment
import com.example.fitgether.presentation.viewmodel.CreateEventViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.SimpleTimeZone
import kotlin.math.min

@AndroidEntryPoint
class SecondCreateSection : Fragment() {

    private var _binding: FragmentSecondCreateSectionBinding? = null
    private val binding get() = _binding!!
    private val updateStepViewmodel : UpdateStepViewmodel by activityViewModels()
    private val createEventViewmodel : CreateEventViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondCreateSectionBinding.inflate(inflater,container,false)

        childFragmentManager.beginTransaction().apply {
            replace(R.id.mapContainer, MapsFragment())
            commit()
        }

        var isLocationProvided = false

        createEventViewmodel.event.observe(viewLifecycleOwner){state ->
            isLocationProvided = state.isLocationProvided
        }




        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            formatDate(calendar)
        }


        binding.eventTimeEditText.setOnClickListener {
            val timePicker = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                binding.eventTimeEditText.setText(time)
                binding.eventTimeInputLayout.error = null

            }

            val timePickerDialog = TimePickerDialog(
                inflater.context,
                R.style.TimePicker,
                timePicker,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )

            timePickerDialog.show()

            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(inflater.context, R.color.green_main))


            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(inflater.context, R.color.green_main))

        }


        binding.eventDateEditText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                inflater.context,
                R.style.AppRegisterDatePicker,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.YEAR, 1)

            val tomorrowDate = Calendar.getInstance()
            tomorrowDate.add(Calendar.DAY_OF_MONTH, 1)

            datePickerDialog.datePicker.maxDate = currentDate.timeInMillis
            datePickerDialog.datePicker.minDate = tomorrowDate.timeInMillis

            datePickerDialog.show()

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(inflater.context,R.color.green_main))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(inflater.context,R.color.green_main))
        }


        binding.secondCreateButton.setOnClickListener {
            eventDateControl()
            eventTimeControl()

            val date = binding.eventDateEditText.text.toString()
            val time = binding.eventTimeEditText.text.toString()

            if(!isLocationProvided){
                Toast.makeText(requireContext(),"Etkinlik konumunu seçmeyi unutma!",Toast.LENGTH_LONG).show()
            }

            if(binding.eventDateInputLayout.error == null &&
                binding.eventTimeInputLayout.error == null &&
                isLocationProvided
            ){

                createEventViewmodel.createEvent(date = date, time = time)
                //burada viewmodela aktarılacak veriler
                updateStepViewmodel.updateStep(2)
            }

        }

        binding.secondBackButton.setOnClickListener{
            updateStepViewmodel.updateStep(0)
        }



        return binding.root
    }

    private fun eventTimeControl() {
        if(binding.eventTimeEditText.text.toString().isEmpty()){
            binding.eventTimeInputLayout.error = "Lütfen bir saat seçin"
        }
    }

    private fun formatDate(calendar: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.eventDateEditText.setText(sdf.format(calendar.time))
        binding.eventDateInputLayout.error = null
    }


    private fun eventDateControl(){
        if(binding.eventDateEditText.text.toString().isEmpty()){
            binding.eventDateInputLayout.error = "Lütfen bir tarih seçin"
        }

    }


}