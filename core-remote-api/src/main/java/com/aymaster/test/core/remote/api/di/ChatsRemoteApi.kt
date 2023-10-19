package com.aymaster.test.core.remote.api.di

import com.aymaster.test.core.remote.api.data.ChatRemoteDataSource

interface ChatsRemoteApi<Message, MessageEvent> {

    fun getChatRemoteDataSource(): ChatRemoteDataSource<Message, MessageEvent>
}