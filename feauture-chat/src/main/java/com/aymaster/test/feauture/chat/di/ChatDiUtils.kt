package com.aymaster.test.feauture.chat.di

import android.app.Application
import android.content.Context

interface ChatFeatureDependenciesProvider {
    val dependencies: ChatFeatureDependencies
}

val Context.moviesDependenciesProvider: ChatFeatureDependenciesProvider
    get() = when(this) {
        is ChatFeatureDependenciesProvider -> this
        is Application -> error("Application should implements ChatFeatureDependenciesProvider")
        else -> applicationContext.moviesDependenciesProvider
    }