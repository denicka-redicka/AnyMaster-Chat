package com.aymaster.test.core.remote.impl.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MessageResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("created_at")
    val createdAt: Long? = null,
    @SerialName("sender_id")
    val senderId: Int? = null,
    @SerialName("read")
    val isRead: Boolean? = null,
    @SerialName("chat_id")
    val chatId: Int? = null,
    @SerialName("loaded")
    val loaded: Boolean? = null
)
