package com.aymaster.test.core.remote.impl.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MessageRequest(
    @SerialName("sender_id")
    val senderId: Int?,
    @SerialName ("message_id")
    val messageId: Int? = null,
    @SerialName("message_body")
    val messageBody: String? = null,
    @SerialName("message_type")
    val messageType: Int?,
    val chatId: Int?,
)
