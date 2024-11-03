package com.example.fitgether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitgether.domain.repository.UserRepository
import com.example.fitgether.utils.AuthErrorType
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(private val repo : UserRepository) : ViewModel() {

    private var _errorState = MutableLiveData<AuthErrorType?>()
    val errorState : LiveData<AuthErrorType?> = _errorState

    fun loginUserAuth(email: String, password : String, isLogged : (Boolean) -> Unit){
        repo.loginUserAuth(email, password){ isSuccesfull , errorType ->
            viewModelScope.launch {
                if(isSuccesfull){
                    _errorState.value = AuthErrorType.NO_ERROR
                    delay(2500)
                    _errorState.value = null
                    isLogged(true)
                }else{
                    _errorState.value = errorType
                    delay(2500)
                    _errorState.value = null
                    isLogged(false)
                }
            }
        }

    }

    fun inputControl(email: String, password: String) : Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }
}