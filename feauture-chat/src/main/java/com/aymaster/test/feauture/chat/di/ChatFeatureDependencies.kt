package com.aymaster.test.feauture.chat.di

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.repository.api.di.ChatRepositoryApi

interface ChatFeatureDependencies {

    fun chatRepository(): ChatRepositoryApi<@JvmSuppressWildcards MessageModel>
}