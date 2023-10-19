package com.aymaster.test.core_local_impl.di

import android.app.Application
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core_local_api.di.ChatLocalApi
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
interface ChatLocalComponent :
    ChatLocalApi<@JvmSuppressWildcards MessageModel> {

    companion object {
        fun initAndGet(app: Application): ChatLocalComponent =
            DaggerChatLocalComponent.builder()
                .application(app)
                .build()
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ChatLocalComponent
    }
}