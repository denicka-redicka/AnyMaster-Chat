package com.aymaster.test.chats

import android.app.Application
import android.content.Context
import com.aymaster.test.chats.di.AppComponent
import com.aymaster.test.chats.di.AppInjector
import com.aymaster.test.chats.di.DaggerAppComponent
import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.remote.api.di.ChatsRemoteApi
import com.aymaster.test.core.remote.impl.di.RemoteComponent
import com.aymaster.test.core.repository.api.di.ChatRepositoryApi
import com.aymaster.test.core_local_api.di.ChatLocalApi
import com.aymaster.test.core_local_impl.di.ChatLocalComponent
import com.aymaster.test.core_repository_impl.di.RepositoryComponent
import com.aymaster.test.core_repository_impl.di.RepositoryDependencies
import com.aymaster.test.feauture.chat.di.ChatFeatureDependencies
import com.aymaster.test.feauture.chat.di.ChatFeatureDependenciesProvider

class ChatApp : Application(), AppInjector, ChatFeatureDependenciesProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appInjector(this)
            .build()
    }

    override fun movieRepository(): ChatRepositoryApi<MessageModel> = RepositoryComponent.initAndGet(
        object : RepositoryDependencies {
            override fun network(): ChatsRemoteApi<MessageModel, MessageEventModel> =
                RemoteComponent.initAndGet()

            override fun dao(): ChatLocalApi<MessageModel> = ChatLocalComponent.initAndGet(this@ChatApp)
        }
    )

    override val dependencies: ChatFeatureDependencies = appComponent
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is ChatApp -> appComponent
        else -> this.applicationContext.appComponent
    }