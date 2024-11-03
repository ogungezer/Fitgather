package com.example.fitgether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeActivityViewModel : ViewModel() {

    private val _permission = MutableLiveData<Boolean>()
    val permission : LiveData<Boolean> get()  = _permission

    fun giveLocationPermission(){
        _permission.value = true
    }
}