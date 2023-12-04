package com.morozov.meetups.domain.model.model

import com.example.chatwithme.domain.model.ChatMessage

data class MessageRegister(
    var chatMessage: ChatMessage,
    var isMessageFromOpponent: Boolean
)