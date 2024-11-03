package com.example.fitgether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitgether.data.model.User
import com.example.fitgether.domain.repository.UserRepository
import com.example.fitgether.utils.AuthErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewmodel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _errorState = MutableLiveData<AuthErrorType?>()
    val errorState : LiveData<AuthErrorType?> = _errorState

    fun registerInputControl(userName : String, email : String, phone : String, name : String, password : String, birthday : String, gender : String) : Boolean{
        if(userName.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && name.isNotBlank() && password.isNotBlank() && birthday.isNotBlank() && gender.isNotBlank()){
            println("inputlar saglandı yani dogru")
            return true
        }
        println("inputlar sağlanamadı")
        return false
    }


    fun registerUserAuth(email: String, password: String, user: User, isUserUnique : (Boolean) -> Unit){
        userRepository.isUserUniqueForRegister(user.userName, user.email, user.phoneNumber){ isUnique, errorType ->
            viewModelScope.launch {
                if(isUnique){
                    _errorState.value = AuthErrorType.NO_ERROR
                    delay(2500)
                    _errorState.value = null
                    userRepository.registerUserAuth(email, password, user)
                    isUserUnique(true)
                }else{
                    isUserUnique(false)
                    _errorState.value = errorType
                    delay(2500)
                    _errorState.value = null

                }
            }

        }
    }

}