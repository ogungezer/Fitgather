package com.example.fitgether.data.repository

import com.example.fitgether.data.FirebaseService
import com.example.fitgether.data.model.Event
import com.example.fitgether.data.model.User
import com.example.fitgether.domain.repository.UserRepository
import com.example.fitgether.utils.AuthErrorType
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val firebaseService: FirebaseService) : UserRepository {

    override fun registerUserAuth(email: String, password: String, user: User) {
        println("REPOIMPL register OK")
        firebaseService.registerUserAuth(email,password, user)
    }

    override fun loginUserAuth(
        email: String,
        password: String,
        isLogged: (Boolean, AuthErrorType) -> Unit
    ) {
        firebaseService.loginUserAuth(email,password, isLogged)
    }

    override fun createNewUser(user: User) {
        firebaseService.createNewUser(user)
    }

    override fun isUserUniqueForRegister(
        username: String,
        email: String,
        phone: String,
        isUnique: (Boolean, AuthErrorType) -> Unit
    ) {
        firebaseService.isUserUniqueForRegister(username,email, phone, isUnique)
    }

    override fun createNewEvent(event: Event) {
        firebaseService.createNewEvent(event)
    }
}