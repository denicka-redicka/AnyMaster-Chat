package com.aymaster.test.core.remote.impl.mock

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.aymaster.test.core.models.MessagesEventStatus
import com.aymaster.test.core.models.toInt
import com.aymaster.test.core.remote.impl.models.request.MessageRequest
import com.aymaster.test.core.remote.impl.models.response.MessageResponse
import com.aymaster.test.core.remote.impl.models.response.WSResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockServerListener @Inject constructor() : WebSocketListener() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        coroutineScope.launch {
            val message = Json.decodeFromString<MessageRequest>(text)
            val messageResponse = WSResponse(
                message.messageType,
                MessageResponse(
                    id = message.messageId,
                    text = message.messageBody,
                    createdAt = Calendar.getInstance().timeInMillis,
                    senderId = message.senderId,
                    chatId = message.chatId,
                    isRead = false,
                    loaded = true
                )
            )
            delay(5000)
            webSocket.send(Json.encodeToString(messageResponse))
            delay(5000)
            if (message.messageType?.equals(MessagesEventStatus.NEW_MESSAGE_TYPE.toInt()) == true) {
                val reactEvent = messageResponse.copy(
                    responseType = MessagesEventStatus.MESSAGE_READ_TYPE.toInt(),
                    message = messageResponse.message?.copy(isRead = true)
                )
                webSocket.send(Json.encodeToString(reactEvent))

                delay(9000)
                val randomSize = IntRange(10, 500).random()
                val loremIpsumString =  LoremIpsum(randomSize).values.iterator().next()
                val newMessage = WSResponse(
                    MessagesEventStatus.NEW_MESSAGE_TYPE.toInt(),
                    MessageResponse(
                        id = message.messageId?.plus(1),
                        text = loremIpsumString,
                        createdAt = Calendar.getInstance().timeInMillis,
                        chatId = message.chatId,
                        senderId = 2,
                        isRead = false
                    )
                )
                webSocket.send(Json.encodeToString(newMessage))
            }
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        webSocket.close(1000, "something goes wrong")
    }
}