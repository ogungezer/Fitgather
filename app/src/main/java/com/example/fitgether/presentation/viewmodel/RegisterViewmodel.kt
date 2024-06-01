package com.example.fitgether.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitgether.data.model.User
import com.example.fitgether.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewmodel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun registerInputControl(userName : String, email : String, phone : String, name : String, password : String, birthday : String, gender : String) : Boolean{
        if(userName.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && name.isNotBlank() && password.isNotBlank() && birthday.isNotBlank() && gender.isNotBlank()){
            println("inputlar saglandı yani dogru")
            return true
        }
        println("inputlar sağlanamadı")
        return false
    }


    fun registerUserAuth(email: String, password: String, user: User){
        viewModelScope.launch {
            println("viewmodel register OK")
            userRepository.registerUserAuth(email, password, user)
        }
    }

}