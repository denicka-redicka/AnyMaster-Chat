package com.aymaster.test.core.remote.impl.data

import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.models.MessagesEventStatus
import com.aymaster.test.core.models.toInt
import com.aymaster.test.core.remote.api.data.ChatRemoteDataSource
import com.aymaster.test.core.remote.impl.mock.MockServerListener
import com.aymaster.test.core.remote.impl.models.request.MessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.concurrent.TaskRunner
import okhttp3.internal.ws.RealWebSocket
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import java.util.Random
import javax.inject.Inject

internal class MockChatRemoteDataSourceImpl @Inject constructor(
    private val chatWSListener: ChatWSListener,
    private val serverListener: MockServerListener,
    private val client: OkHttpClient
) : ChatRemoteDataSource<MessageModel, MessageEventModel> {

    private val random = Random()
    private val mockWebServer = MockWebServer()

    private var webSocket: RealWebSocket? = null

    override suspend fun getAllMessages(): List<MessageModel> {
        //TODO
        return listOf()
    }

    override suspend fun getWSEvents(): Flow<MessageEventModel> = chatWSListener.getEventsFlow()

    override fun getSocketConnectionState(): Flow<Boolean> = chatWSListener.getSocketConnectionState()

    override suspend fun openWSConnection() {
        webSocket = RealWebSocket(
            TaskRunner.INSTANCE, Request.Builder().get().url(mockWebServer.url("/")).build(), chatWSListener,
            random, client.pingIntervalMillis.toLong(), null, 0L
        )
        mockWebServer.enqueue(MockResponse().withWebSocketUpgrade(serverListener))
        webSocket?.connect(client)
    }

    override suspend fun closeConnection() {
        webSocket?.close(1000, "Not needed anymore")
    }

    override suspend fun sendMessage(chatId: Int, message: MessageModel) {
        val newMessage = MessageRequest(
            senderId = message.senderId,
            messageType = MessagesEventStatus.NEW_MESSAGE_TYPE.toInt(),
            messageBody = message.textBody,
            messageId = message.id,
            chatId = chatId
        )
        webSocket?.send(Json.encodeToString(newMessage))
    }

    override suspend fun editMessage(chatId: Int, message: MessageModel) {
        val editedMessage = MessageRequest(
            senderId = message.id,
            messageBody = message.textBody,
            messageType = MessagesEventStatus.EDIT_MESSAGE_TYPE.toInt(),
            messageId = message.senderId,
            chatId = chatId
        )
        webSocket?.send(Json.encodeToString(editedMessage))
    }

    override suspend fun deleteMessage(chatId: Int, messageId: Int) {
        val deleteMessage = MessageRequest(
            senderId = MOCK_ID,
            messageType = MessagesEventStatus.DELETE_MESSAGE_TYPE.toInt(),
            messageId = messageId,
            chatId = chatId
        )
        webSocket?.send(Json.encodeToString(deleteMessage))
    }

    override suspend fun readMessage(chatId: Int, messageId: Int) {
        val readMessage = MessageRequest(
            senderId = MOCK_PARTNER_ID, //Should get from userData
            messageType = MessagesEventStatus.MESSAGE_READ_TYPE.toInt(),
            messageId = messageId,
            chatId = chatId
        )
        webSocket?.send(Json.encodeToString(readMessage))
    }

    private companion object {
        const val MOCK_ID = 1
        const val MOCK_PARTNER_ID = 2
    }
}