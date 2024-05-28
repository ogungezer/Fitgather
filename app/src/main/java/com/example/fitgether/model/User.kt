package com.example.fitgether.model

data class User(
    val userId : String = "",
    val userName : String = "",
    val gender : String = "",
    val fullName : String = "",
    val email : String = "",
    val profilePicture : String? = "",
    val location : String = "",
    val rating : Float? = null,
    val password : String = "",
    val createdEvents : List<String> = listOf(),
    val joinedEvents : List<String> = listOf()
)
