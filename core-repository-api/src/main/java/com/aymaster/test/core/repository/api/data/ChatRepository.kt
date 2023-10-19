package com.aymaster.test.core.repository.api.data

import kotlinx.coroutines.flow.Flow

interface ChatRepository<Message> {

    suspend fun getAllMessages(chatId: Int): Flow<List<Message>>

    suspend fun openWSConnection()

    suspend fun closeConnection()

    fun getSocketConnectionState(): Flow<Boolean>

    suspend fun sendMessage(chatId: Int,messageText: String)

    suspend fun editMessage(chatId: Int, messageId: Int, newMessageText: String)

    suspend fun deleteMessage(chatId: Int, messageId: Int)

    suspend fun readMessage(chatId: Int, messageId: Int)
}