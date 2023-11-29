package com.example.chatwithme.domain.usecase.authScreen

import com.morozov.meetups.domain.repository.AuthScreenRepository

class IsUserAuthenticatedInFirebase(
    private val authScreenRepository: AuthScreenRepository
) {
    operator fun invoke() = authScreenRepository.isUserAuthenticatedInFirebase()
}