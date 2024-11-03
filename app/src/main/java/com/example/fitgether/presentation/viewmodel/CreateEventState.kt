package com.example.fitgether.presentation.viewmodel

import com.example.fitgether.data.model.Event

data class CreateEventState(
    val event: Event = Event(),
    var isLocationProvided : Boolean = false
)