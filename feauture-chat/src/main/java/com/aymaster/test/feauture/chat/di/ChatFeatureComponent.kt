package com.aymaster.test.feauture.chat.di

import com.aymaster.test.feauture.chat.presentation.fragment.ChatFragment
import com.aymaster.test.core.di.Feature
import dagger.Component

@Feature
@Component (dependencies = [ChatFeatureDependencies::class])
interface ChatFeatureComponent {

    fun inject(fragment: ChatFragment)

    companion object {
        fun initAndGet(dependencies: ChatFeatureDependencies): ChatFeatureComponent =
            DaggerChatFeatureComponent.builder()
                .chatFeatureDependencies(dependencies)
                .build()
    }
}