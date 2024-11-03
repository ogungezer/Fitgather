package com.example.fitgether.domain.repository

import com.example.fitgether.data.model.Event
import com.example.fitgether.data.model.User
import com.example.fitgether.utils.AuthErrorType

interface UserRepository {

    fun registerUserAuth(email : String, password : String, user: User)
    fun loginUserAuth(email : String, password : String, isLogged : (Boolean,AuthErrorType) -> Unit)
    fun createNewUser(user : User)
    fun isUserUniqueForRegister(username : String, email: String, phone : String, isUnique : (Boolean, AuthErrorType) -> Unit)
    fun createNewEvent(event: Event)

}