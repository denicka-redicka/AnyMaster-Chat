package com.aymaster.test.feauture.chat.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ChatComponentViewModel(application: Application): AndroidViewModel(application) {

    val contactsListComponent: ChatFeatureComponent by lazy {
        ChatFeatureComponent.initAndGet(application.moviesDependenciesProvider.dependencies)
    }
}