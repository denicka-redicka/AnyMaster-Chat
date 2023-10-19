package com.aymaster.test.core.remote.impl.di

import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.remote.api.di.ChatsRemoteApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ChatNetworkModule::class])
interface RemoteComponent : ChatsRemoteApi<@JvmSuppressWildcards MessageModel, @JvmSuppressWildcards MessageEventModel> {

    companion object {
        fun initAndGet(): RemoteComponent = DaggerRemoteComponent.builder()
            .build()
    }
}