package com.aymaster.test.core.remote.api.data

import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource<Message, MessageEvent> {

    suspend fun getAllMessages() : List<Message>

    suspend fun getWSEvents(): Flow<MessageEvent>

    fun getSocketConnectionState(): Flow<Boolean>

    suspend fun openWSConnection()

    suspend fun closeConnection()

    suspend fun sendMessage(chatId: Int,message: Message)

    suspend fun editMessage(chatId: Int,message: Message)

    suspend fun deleteMessage(chatId: Int, messageId: Int)

    suspend fun readMessage(chatId: Int,messageId: Int)
}