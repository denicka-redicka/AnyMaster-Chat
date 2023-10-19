package com.aymaster.test.core.remote.impl.retrofit

import com.aymaster.test.core.remote.impl.models.response.MessageResponse
import retrofit2.http.GET

internal interface ChatRestApi {

    @GET("")
    suspend fun getAllMessages(): List<MessageResponse>?
}