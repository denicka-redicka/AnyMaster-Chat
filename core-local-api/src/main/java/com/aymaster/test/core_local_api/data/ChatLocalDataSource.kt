package com.aymaster.test.core_local_api.data

import kotlinx.coroutines.flow.Flow

interface ChatLocalDataSource<Message> {

    fun getMessages(chatId: Int): Flow<List<Message>>

    suspend fun getMessageById(chatId: Int, messageId: Int ): Message

    suspend fun saveMessages(messages: List<Message>)

    suspend fun updateMessage(newMessage: Message)

    suspend fun deleteMessage(messageId: Int)

    suspend fun getLastMessageId(chatId: Int): Int
}