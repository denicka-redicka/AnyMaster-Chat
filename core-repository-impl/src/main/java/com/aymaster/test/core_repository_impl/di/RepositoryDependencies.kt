package com.aymaster.test.core_repository_impl.di

import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.remote.api.di.ChatsRemoteApi
import com.aymaster.test.core_local_api.di.ChatLocalApi

interface RepositoryDependencies {

    fun network(): ChatsRemoteApi<@JvmSuppressWildcards MessageModel, @JvmSuppressWildcards MessageEventModel>

    fun dao(): ChatLocalApi<MessageModel>
}