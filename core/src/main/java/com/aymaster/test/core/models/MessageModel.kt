package com.aymaster.test.core.models

data class MessageModel(
    val updatedAt: Long,
    val id: Int,
    val textBody: String,
    val isRead: Boolean,
    val senderId: Int,
    val loaded: Boolean,
    val chatId: Int,
) {
    companion object {
        val EMPTY_MESSAGE = MessageModel(
            updatedAt = 0,
            id = 0,
            textBody = "",
            isRead = false,
            senderId = 0,
            loaded = false,
            chatId = 0
        )
    }
}
