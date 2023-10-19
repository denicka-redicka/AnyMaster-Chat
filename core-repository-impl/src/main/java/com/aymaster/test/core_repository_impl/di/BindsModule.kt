package com.aymaster.test.core_repository_impl.di

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.repository.api.data.ChatRepository
import com.aymaster.test.core_repository_impl.data.ChatRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
internal interface BindsModule {

    @Binds
    fun provideMoviesRepository(implementation: ChatRepositoryImpl): ChatRepository<@JvmSuppressWildcards MessageModel>
}