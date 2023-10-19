package com.aymaster.test.chats.di

import com.aymaster.test.chats.MainActivity
import com.aymaster.test.feauture.chat.di.ChatFeatureDependencies
import dagger.Component
import javax.inject.Singleton

@[Singleton Component(dependencies = [AppInjector::class])]
interface AppComponent : ChatFeatureDependencies {

    fun inject(activity: MainActivity)
}