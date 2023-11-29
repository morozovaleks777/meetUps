package com.morozov.meetups.domain.usecase.profileScreen

import com.morozov.meetups.domain.repository.ProfileScreenRepository

class SignOut(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke() = profileScreenRepository.signOut()
}