package com.example.chatwithme.domain.usecase.userListScreen

import com.morozov.meetups.domain.repository.UserListScreenRepository

class AcceptPendingFriendRequestToFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(registerUUID: String) =
        userListScreenRepository.acceptPendingFriendRequestToFirebase(registerUUID)
}