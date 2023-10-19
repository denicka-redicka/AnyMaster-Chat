package com.aymaster.test.core_repository_impl.di

import com.aymaster.test.core.di.Feature
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.repository.api.di.ChatRepositoryApi
import dagger.Component

@[Feature Component(
    modules = [ChatRepositoryModule::class],
    dependencies = [RepositoryDependencies::class]
)]
interface RepositoryComponent : ChatRepositoryApi<MessageModel> {

    companion object {
        fun initAndGet(dependencies: RepositoryDependencies): RepositoryComponent =
            DaggerRepositoryComponent.builder()
                .repositoryDependencies(dependencies)
                .build()
    }
}