package com.aymaster.test.core.remote.impl.mappers

import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.models.MessagesEventStatus
import com.aymaster.test.core.remote.impl.models.response.WSResponse

internal fun WSResponse.toMessageEventModel(): MessageEventModel = MessageEventModel(
    event = MessagesEventStatus.fromInt(this.responseType?: 0),
    message = this.message?.toMessageModel()?: MessageModel.EMPTY_MESSAGE
)