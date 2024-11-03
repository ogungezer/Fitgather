package com.example.fitgether.presentation.view.createFragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class UpdateStepViewmodel : ViewModel() {
    private val _currentStep = MutableLiveData(0)
    val currentStep: LiveData<Int> get() = _currentStep

    fun updateStep(step: Int) {
        _currentStep.value = step
        println("step viewmodeldeki $step")
        println("viewmodele girdi ${currentStep.value}")

    }


}