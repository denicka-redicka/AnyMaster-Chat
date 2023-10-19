package com.aymaster.test.core_repository_impl.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module(includes = [BindsModule::class])
class ChatRepositoryModule {

    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}