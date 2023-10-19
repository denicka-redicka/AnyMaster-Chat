package com.aymaster.test.core.repository.api.di

import com.aymaster.test.core.repository.api.data.ChatRepository

interface ChatRepositoryApi<Message> {

    fun getChatRepository(): ChatRepository<Message>
}