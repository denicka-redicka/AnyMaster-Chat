package com.aymaster.test.core.remote.impl.mappers

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.remote.impl.models.response.MessageResponse

internal fun MessageResponse.toMessageModel(): MessageModel = MessageModel(
    updatedAt = this.createdAt?: 0,
    id = this.id?: 0,
    textBody = this.text?: "",
    isRead = this.isRead?: false,
    senderId = this.senderId?: 0,
    chatId = this.chatId?: 0,
    loaded = this.loaded?: false
)