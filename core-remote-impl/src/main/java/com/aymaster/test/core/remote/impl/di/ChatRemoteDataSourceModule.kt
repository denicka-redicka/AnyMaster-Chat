package com.aymaster.test.core.remote.impl.di

import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.remote.api.data.ChatRemoteDataSource
import com.aymaster.test.core.remote.impl.data.MockChatRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ChatRemoteDataSourceModule {

    @Binds
    fun bindRemoteDataSource(implementation: MockChatRemoteDataSourceImpl):
        ChatRemoteDataSource<@JvmSuppressWildcards MessageModel, @JvmSuppressWildcards MessageEventModel>
}