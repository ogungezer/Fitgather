package com.example.fitgether.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class FormatPhoneNumber(private val phoneText : TextInputEditText) : TextWatcher {

    private val regexOne = """(\d{3})(\d{3})(\d{2})(\d{1})""".toRegex()
    private val regexTwo = """(\d{3})(\d{3})(\d{1})""".toRegex()
    private val regexThree = """(\d{3})(\d{1})""".toRegex()
    private val regexFour = """(\d{1})""".toRegex()

    private var isFormatting: Boolean = false


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //
    }

    override fun afterTextChanged(s: Editable?) {
        if (!isFormatting) {
            val phoneNumber = s.toString().filter { it.isDigit() }
            val formattedText = formatPhoneNumber(phoneNumber)
            isFormatting = true
            phoneText.setText(formattedText)
            phoneText.setSelection(formattedText.length)
            isFormatting = false
        }
    }
    //5383826129
    private fun formatPhoneNumber(phoneNumber: String): String {
        return if(phoneNumber.length >=9){
            regexOne.replace(phoneNumber, "($1) $2 $3 $4")
        } else if(phoneNumber.length >= 7){
            regexTwo.replace(phoneNumber, "($1) $2 $3")
        } else if(phoneNumber.length >= 4){
            regexThree.replace(phoneNumber, "($1) $2")
        } else if(phoneNumber.isNotEmpty()) {
            regexFour.replace(phoneNumber, "$1")
        } else{
            phoneNumber
        }
    }

}