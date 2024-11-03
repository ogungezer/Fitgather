package com.example.fitgether.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitgether.data.model.Event
import com.example.fitgether.data.model.User
import com.example.fitgether.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewmodel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _event = MutableLiveData(CreateEventState(isLocationProvided = false))
    val event : LiveData<CreateEventState> get()  = _event


    fun createEvent(
        category: String? = null,
        title: String? = null,
        description: String? = null,
        location: List<Double>? = null,
        address: String? = null,
        date: String? = null,
        time: String? = null,
        genderChoice: String? = null,
        maxPersonCount: Int? = null,
        eventCreatorId : String? = null,
    ){
            val currentState = _event.value ?: CreateEventState()
            val updatedEventState = currentState.event.copy(
                eventCreatorId = eventCreatorId ?: currentState.event.eventCreatorId,
                eventCategory = category ?: currentState.event.eventCategory,
                eventTitle = title ?: currentState.event.eventTitle,
                eventDescription = description ?: currentState.event.eventDescription,
                eventLocation = location ?: currentState.event.eventLocation,
                eventAddress = address ?: currentState.event.eventAddress,
                eventDate = date ?: currentState.event.eventDate,
                eventTime = time ?: currentState.event.eventTime,
                genderChoice = genderChoice ?: currentState.event.genderChoice,
                maxPerson = maxPersonCount ?: currentState.event.maxPerson,
            )
            _event.value = currentState.copy(
                event = updatedEventState
            )

    }

    fun pushCreatedEventToDb(){
        viewModelScope.launch {
            event.value?.event?.let {
                repository.createNewEvent(it)
            }
            _event.value = CreateEventState() //db ye pushladıktan sonra eski haline getirildi state
        }
    }

    fun locationControl(isProvided : Boolean){
        val currentState = _event.value ?: CreateEventState()
        _event.value = currentState.copy(isLocationProvided = isProvided)
        println(event.value?.isLocationProvided)
    }




}