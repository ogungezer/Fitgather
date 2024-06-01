package com.example.fitgether.domain.repository

import com.example.fitgether.data.model.User

interface UserRepository {

    fun registerUserAuth(email : String, password : String, user: User)
    fun loginUserAuth(email : String, password : String)
    fun createNewUser(user : User)

}