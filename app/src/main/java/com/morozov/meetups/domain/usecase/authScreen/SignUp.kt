package com.example.chatwithme.domain.usecase.authScreen

import com.morozov.meetups.domain.repository.AuthScreenRepository

class SignUp(
    private val authScreenRepository: AuthScreenRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authScreenRepository.signUp(email, password)
}