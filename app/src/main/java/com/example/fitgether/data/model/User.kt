package com.example.fitgether.data.model

import java.io.Serializable

data class User(
    val userId : String? = "",
    val userName : String = "",
    val gender : String = "",
    val fullName : String = "",
    val email : String = "",
    val profilePicture : String? = "",
    val phoneNumber : String = "",
    val birthday : String = "",
    val location : String = "",
    val rating : Float? = null,
    val createdEvents : List<String> = listOf(),
    val joinedEvents : List<String> = listOf()
) : Serializable
