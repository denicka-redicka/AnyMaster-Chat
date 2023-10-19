package com.aymaster.test.core_local_impl.di

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core_local_api.data.ChatLocalDataSource
import com.aymaster.test.core_local_impl.data.ChatLocalDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ChatDataSourceModule {

    @Binds
    fun bindLocalDataSource(implementation: ChatLocalDataSourceImpl):
        ChatLocalDataSource<@JvmSuppressWildcards MessageModel>
}