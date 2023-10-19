package com.aymaster.test.core_local_impl.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MessagesDao {

    @Query("SELECT * from messages WHERE chat_id = :chatId ORDER BY updated_at DESC")
    fun getAllMessages(chatId: Int): Flow<List<Message>>

    @Query("SELECT * from messages WHERE chat_id = :chatId AND message_id = :messageId")
    suspend fun getMessageById(chatId: Int, messageId: Int): Message

    @Update
    suspend fun updateMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveMessage(message: Message)

    @Query("DELETE FROM messages WHERE message_id = :id")
    suspend fun removeMessage(id: Int)

    @Query("SELECT message_id FROM messages WHERE chat_id = :chatId ORDER BY updated_at ASC")
    suspend fun getMessageIds(chatId: Int): List<Int>?
}