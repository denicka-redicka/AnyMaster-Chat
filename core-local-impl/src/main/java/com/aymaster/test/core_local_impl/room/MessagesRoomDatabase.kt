package com.aymaster.test.core_local_impl.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
internal abstract class MessagesRoomDatabase : RoomDatabase() {

    abstract fun getMessagesDao(): MessagesDao

    companion object {
        private const val MESSAGES_DATABASE = "messages.db"

        fun DaoInstance(context: Context): MessagesRoomDatabase {
            return Room.databaseBuilder(
                context,
                MessagesRoomDatabase::class.java,
                MESSAGES_DATABASE
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}