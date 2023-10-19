package com.aymaster.test.core.remote.impl.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class WSResponse(
    @SerialName("type")
    val responseType: Int? = null,
    @SerialName("message")
    val message: MessageResponse? = null
)
