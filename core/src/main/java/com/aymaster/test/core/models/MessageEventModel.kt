package com.aymaster.test.core.models

data class MessageEventModel(
    val event: MessagesEventStatus,
    val message: MessageModel
)
