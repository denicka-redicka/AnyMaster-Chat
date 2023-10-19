package com.aymaster.test.core_local_api.di

import com.aymaster.test.core_local_api.data.ChatLocalDataSource

interface ChatLocalApi<Message> {

    fun getLocalDataSource(): ChatLocalDataSource<Message>
}