package com.morozov.meetups.domain.usecase.chatScreen

import com.example.chatwithme.domain.usecase.chatScreen.InsertMessageToFirebase
import com.example.chatwithme.domain.usecase.chatScreen.LoadMessageFromFirebase
import com.example.chatwithme.domain.usecase.chatScreen.LoadOpponentProfileFromFirebase

data class ChatScreenUseCases(
    val blockFriendToFirebase: BlockFriendToFirebase,
    val insertMessageToFirebase: InsertMessageToFirebase,
    val loadMessageFromFirebase: LoadMessageFromFirebase,
    val opponentProfileFromFirebase: LoadOpponentProfileFromFirebase
)