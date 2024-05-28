package com.example.fitgether.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextWatcher
import android.text.method.TextKeyListener.Capitalize
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.util.LocalePreferences.CalendarType
import androidx.core.text.util.LocalePreferences.CalendarType.CalendarTypes
import androidx.core.widget.doOnTextChanged
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentLoginBinding
import com.example.fitgether.databinding.FragmentRegisterBinding
import com.example.fitgether.utils.FormatPhoneNumber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)

        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            formatDate(calendar)
        }

        //uygulama 15 yaş üzeri olduğundan dolayı kayıt olurken tarih mevcut yılın 15 yıl gerisine kadar seçilebilecek.
        val myCalendar = Calendar.getInstance()
        myCalendar.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR) - 15)
        myCalendar.set(Calendar.MONTH, Calendar.DECEMBER)
        myCalendar.set(Calendar.DAY_OF_MONTH,31)

        val phoneNumber = binding.phoneEdtTxt
        binding.phoneEdtTxt.addTextChangedListener(FormatPhoneNumber(phoneNumber))

        binding.birthEdtTxt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                inflater.context,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = myCalendar.timeInMillis


            datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal") { dialog, _ ->
                dialog.dismiss()
            }

            datePickerDialog.show()
        }

        userNameControl()
        nameControl()
       // phoneControl()

        binding.registerBtn.setOnClickListener {
            //buradakayıtiçinfirebasefirestoreveauth, ve navigation logine
        }

        return binding.root
    }


    private fun nameControl() {
        binding.nameEdtTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(text.any{it.isDigit()}){
                    binding.nameInputLayout.error = "İsim sayı içermemeli."
                }else if(text.toString().replace(" ","").any { !it.isLetter() }){
                    binding.nameInputLayout.error = "İsim harf harici sembol içermemeli."
                }else{
                    binding.nameInputLayout.error = null
                }
            }
        }
    }

    private fun userNameControl() {
        binding.usernameEdtTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(text.length < 6){
                    binding.usernameInputLayout.error = "Kullanıcı adı en az 6 karakter olmalı."
                } else if(text.any{ it.isUpperCase()}){
                    binding.usernameInputLayout.error = "Kullanıcı adı büyük harf içeremez."
                } else{
                    binding.usernameInputLayout.error = null
                }
            }
        }
    }

    private fun formatDate(calendar: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.birthEdtTxt.setText(sdf.format(calendar.time))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}