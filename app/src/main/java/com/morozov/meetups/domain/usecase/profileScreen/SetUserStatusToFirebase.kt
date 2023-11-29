package com.morozov.meetups.domain.usecase.profileScreen

import com.example.chatwithme.domain.model.UserStatus
import com.morozov.meetups.domain.repository.ProfileScreenRepository

class SetUserStatusToFirebase(
    private val profileScreenRepository: ProfileScreenRepository
) {
    suspend operator fun invoke(userStatus: UserStatus) =
        profileScreenRepository.setUserStatusToFirebase(userStatus)
}