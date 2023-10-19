package com.aymaster.test.core_local_impl.utils

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core_local_impl.room.Message

internal fun MessageModel.toMessageDb(): Message = Message(
    messageId = this.id,
    messageBody = this.textBody,
    senderId = this.senderId,
    updatedAt = this.updatedAt,
    isRead = this.isRead,
    loaded = this.loaded,
    chatId = this.chatId
)

internal fun Message.toMessageModel(): MessageModel = MessageModel(
    id = this.messageId,
    textBody = this.messageBody,
    senderId = this.senderId,
    updatedAt = this.updatedAt,
    isRead = this.isRead,
    loaded = this.loaded,
    chatId = this.chatId
)