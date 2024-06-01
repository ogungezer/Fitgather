package com.example.fitgether.presentation.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.text.TextWatcher
import android.text.method.TextKeyListener.Capitalize
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.util.LocalePreferences.CalendarType
import androidx.core.text.util.LocalePreferences.CalendarType.CalendarTypes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fitgether.R
import com.example.fitgether.databinding.FragmentLoginBinding
import com.example.fitgether.databinding.FragmentRegisterBinding
import com.example.fitgether.presentation.viewmodel.RegisterViewmodel
import com.example.fitgether.utils.FormatPhoneNumber
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewmodel by viewModels<RegisterViewmodel>()
    private lateinit var auth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        binding.phoneInputLayout.prefixTextView.textSize = 12f
        activity?.window?.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)

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
        mailControl()
        passwordControl()
        phoneControl()

        binding.loginTxtBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.registerBtn.setOnClickListener {

            val username = binding.usernameEdtTxt.text.toString()
            val name = binding.nameEdtTxt.text.toString()
            val email = binding.emailEdtTxt.text.toString()
            val phone = binding.phoneEdtTxt.text.toString()
            val password = binding.passwordEdtTxt.text.toString()
            val birthday = binding.birthEdtTxt.text.toString()
            var gender = ""
            if(binding.radioGroupRegister.checkedRadioButtonId != -1){
                val selectedId = binding.radioGroupRegister.checkedRadioButtonId
                val selectedGender = binding.root.findViewById<RadioButton>(selectedId)
                gender = selectedGender.text.toString()
            }

            if(binding.usernameInputLayout.error == null && binding.phoneInputLayout.error == null && binding.nameInputLayout.error == null && binding.emailInputLayout.error == null
                && binding.passwordInputLayout.error == null && binding.birthInputLayout.error == null && gender != ""){
                when(registerViewmodel.registerInputControl(username,email,phone,name,password,birthday,gender)){
                    true -> {
                        val newUser = com.example.fitgether.data.model.User(
                            userName = username,
                            gender = gender,
                            fullName = name,
                            email = email,
                            birthday = birthday,
                            phoneNumber = phone
                        )
                        registerViewmodel.registerUserAuth(email, password, newUser)
                        Toast.makeText(requireContext(),"Kullanıcı kaydı başarılı", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    false -> {
                        Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun.", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Lütfen girdileri gözden geçirin.", Toast.LENGTH_LONG).show()
            }

        }

        return binding.root
    }

    private fun phoneControl() {
        binding.phoneEdtTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(text.length > 4){
                    if(text[1] == '0'){
                        binding.phoneInputLayout.error = "Numaranızı başında (0) olmadan giriniz."
                    }else if (text[1] != '5'){
                        binding.phoneInputLayout.error = "Lütfen geçerli bir numara giriniz."
                    }else{
                        binding.phoneInputLayout.error = null
                    }
                }else if(text.isNotEmpty()) {
                    if(text[0] == '0'){
                        binding.phoneInputLayout.error = "Numaranızı başında (0) olmadan giriniz."
                    }else if (text[0] != '5'){
                        binding.phoneInputLayout.error = "Lütfen geçerli bir numara giriniz."
                    }
                }else{
                    binding.phoneInputLayout.error = null
                }
            }
        }
    }

    private fun passwordControl() {
        binding.passwordEdtTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(text.length < 8){
                    binding.passwordInputLayout.errorIconDrawable = null
                    binding.passwordInputLayout.error = "Şifre en az 8 karakterden oluşmalı."
                }else if(isPasswordOnlyDigit(text.toString())){
                    binding.passwordInputLayout.errorIconDrawable = null
                    binding.passwordInputLayout.error = "Şifre sadece sayılardan oluşamaz."
                }else{
                    binding.passwordInputLayout.errorIconDrawable = null
                    binding.passwordInputLayout.error = null
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

    private fun mailControl() {
        binding.emailEdtTxt.doOnTextChanged { text, start, before, count ->
            text?.let {
                if(isEmailValid(text)){
                    binding.emailInputLayout.error = null
                }else{
                    binding.emailInputLayout.error = "Geçerli bir mail formatı giriniz."
                }
            }
        }
    }

    private fun isEmailValid(email : CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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