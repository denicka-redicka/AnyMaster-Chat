package com.aymaster.test.chats.di

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.repository.api.di.ChatRepositoryApi

interface AppInjector {

    fun movieRepository(): ChatRepositoryApi<@JvmSuppressWildcards MessageModel>
}