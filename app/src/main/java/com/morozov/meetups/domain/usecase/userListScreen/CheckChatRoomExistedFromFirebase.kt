package com.example.chatwithme.domain.usecase.userListScreen

import com.morozov.meetups.domain.repository.UserListScreenRepository

class CheckChatRoomExistedFromFirebase(
    private val userListScreenRepository: UserListScreenRepository
) {
    suspend operator fun invoke(acceptorUUID: String) =
        userListScreenRepository.checkChatRoomExistedFromFirebase(acceptorUUID)
}