package com.aymaster.test.core_local_impl.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aymaster.test.core_local_impl.utils.MESSAGE_TABLE_NAME

@Entity(MESSAGE_TABLE_NAME)
internal data class Message (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "message_id")
    val messageId: Int,
    @ColumnInfo(name = "loaded")
    val loaded: Boolean,
    @ColumnInfo(name = "message_body")
    val messageBody: String,
    @ColumnInfo(name = "sender_id")
    val senderId: Int,
    @ColumnInfo(name = "chat_id")
    val chatId: Int,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "is_read")
    val isRead: Boolean,
)