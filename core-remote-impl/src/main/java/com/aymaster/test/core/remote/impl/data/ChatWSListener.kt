package com.aymaster.test.core.remote.impl.data

import android.util.Log
import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.remote.impl.mappers.toMessageEventModel
import com.aymaster.test.core.remote.impl.models.response.WSResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class ChatWSListener @Inject constructor(
    private val json: Json
) : WebSocketListener() {

    private val newEventFlow: MutableSharedFlow<MessageEventModel> = MutableSharedFlow()
    private val connectedFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun getSocketConnectionState(): Flow<Boolean> = connectedFlow
    fun getEventsFlow(): Flow<MessageEventModel> = newEventFlow

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("webSocketListener", "onOpen() called with client")
        connectedFlow.value = true
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val event = json.decodeFromString<WSResponse>(text)
        CoroutineScope(Dispatchers.IO).launch {
            newEventFlow.emit(event.toMessageEventModel())
        }
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        connectedFlow.value = false
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d("websocketListener", "onFailure() called with: webSocket = $webSocket, t = $t, response = $response")
    }
}