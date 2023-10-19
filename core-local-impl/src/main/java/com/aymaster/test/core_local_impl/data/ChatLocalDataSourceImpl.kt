package com.aymaster.test.core_local_impl.data

import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core_local_api.data.ChatLocalDataSource
import com.aymaster.test.core_local_impl.room.MessagesDao
import com.aymaster.test.core_local_impl.utils.toMessageDb
import com.aymaster.test.core_local_impl.utils.toMessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ChatLocalDataSourceImpl @Inject constructor(
    private val messagesDao: MessagesDao
) : ChatLocalDataSource<@JvmSuppressWildcards MessageModel> {

    override fun getMessages(chatId: Int): Flow<List<MessageModel>> =
        messagesDao.getAllMessages(chatId).map { dbMessages ->
            dbMessages.map { dbMessage -> dbMessage.toMessageModel() }
        }

    override suspend fun getMessageById(chatId: Int, messageId: Int): MessageModel {
        val message = messagesDao.getMessageById(chatId, messageId)
        return message.toMessageModel()
    }

    override suspend fun deleteMessage(messageId: Int) {
        messagesDao.removeMessage(messageId)
    }

    override suspend fun getLastMessageId(chatId: Int): Int {
        val ids = messagesDao.getMessageIds(chatId)
        return ids?.lastOrNull() ?: 1
    }

    override suspend fun updateMessage(newMessage: MessageModel) {
        messagesDao.updateMessage(newMessage.toMessageDb())
    }

    override suspend fun saveMessages(messages: List<MessageModel>) {
        messages.forEach { message ->
            messagesDao.saveMessage(message.toMessageDb())
        }
    }
}