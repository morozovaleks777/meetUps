package com.morozov.meetups.domain.usecase.profileScreen

import com.morozov.meetups.domain.repository.ProfileScreenRepository

class LoadProfileFromFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke() = profileScreenRepository.loadProfileFromFirebase()
}