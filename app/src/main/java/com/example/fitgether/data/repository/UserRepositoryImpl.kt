package com.example.fitgether.data.repository

import com.example.fitgether.data.FirebaseService
import com.example.fitgether.data.model.User
import com.example.fitgether.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val firebaseService: FirebaseService) : UserRepository {

    override fun registerUserAuth(email: String, password: String, user: User) {
        println("REPOIMPL register OK")
        firebaseService.registerUserAuth(email,password, user)
    }

    override fun loginUserAuth(email: String, password: String) {
        //
    }

    override fun createNewUser(user: User) {
        firebaseService.createNewUser(user)
    }
}