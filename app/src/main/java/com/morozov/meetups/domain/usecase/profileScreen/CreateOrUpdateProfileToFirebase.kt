package com.morozov.meetups.domain.usecase.profileScreen

import com.morozov.meetups.domain.repository.ProfileScreenRepository
import com.morozov.meetups.domain.model.model.User

class CreateOrUpdateProfileToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(user: User) =
        profileScreenRepository.createOrUpdateProfileToFirebase(user)
}