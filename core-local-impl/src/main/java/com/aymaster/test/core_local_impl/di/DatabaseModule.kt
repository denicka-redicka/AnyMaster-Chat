package com.aymaster.test.core_local_impl.di

import android.app.Application
import com.aymaster.test.core_local_impl.room.MessagesDao
import com.aymaster.test.core_local_impl.room.MessagesRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ChatDataSourceModule::class])
internal class DatabaseModule {

    @Provides
    @Singleton
    fun provideMessagesDatabase(application: Application): MessagesRoomDatabase =
        MessagesRoomDatabase.DaoInstance(application)

    @Provides
    @Singleton
    fun provideMovieDao(db: MessagesRoomDatabase): MessagesDao = db.getMessagesDao()
}