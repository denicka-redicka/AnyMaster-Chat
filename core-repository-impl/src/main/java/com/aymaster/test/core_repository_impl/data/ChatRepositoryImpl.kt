package com.aymaster.test.core_repository_impl.data

import com.aymaster.test.core.models.MessageEventModel
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.models.MessagesEventStatus
import com.aymaster.test.core.remote.api.di.ChatsRemoteApi
import com.aymaster.test.core.repository.api.data.ChatRepository
import com.aymaster.test.core_local_api.di.ChatLocalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

internal class ChatRepositoryImpl @Inject constructor(
    networkApi: ChatsRemoteApi<@JvmSuppressWildcards MessageModel, @JvmSuppressWildcards MessageEventModel>,
    localApi: ChatLocalApi<@JvmSuppressWildcards MessageModel>,
    repositoryScope: CoroutineScope
) : ChatRepository<MessageModel> {

    private val remoteDataSource = networkApi.getChatRemoteDataSource()
    private val localDataSource = localApi.getLocalDataSource()

    init {
        repositoryScope.launch(Dispatchers.IO) {

            (remoteDataSource.getWSEvents() as? Flow<MessageEventModel>)
                ?.collect { messageEvent ->
                    when (messageEvent.event) {
                        MessagesEventStatus.NEW_MESSAGE_TYPE -> if (messageEvent.message.senderId == MOCK_USER_ID) {
                            localDataSource.updateMessage(messageEvent.message)
                        } else {
                            localDataSource.saveMessages(listOf(messageEvent.message))
                        }
                        MessagesEventStatus.DELETE_MESSAGE_TYPE -> localDataSource.deleteMessage(messageEvent.message.id)
                        MessagesEventStatus.EDIT_MESSAGE_TYPE -> localDataSource.updateMessage(
                            messageEvent.message
                        )
                        MessagesEventStatus.MESSAGE_READ_TYPE -> {
                            val savedUnreadMessage = localDataSource.getMessageById(
                                chatId = messageEvent.message.chatId,
                                messageId = messageEvent.message.id
                            )
                            localDataSource.updateMessage(
                                savedUnreadMessage.copy(isRead = true)
                            )
                        }
                        MessagesEventStatus.UNDEFINED -> {}
                    }
                }
        }
    }

    override suspend fun getAllMessages(chatId: Int): Flow<List<MessageModel>> = localDataSource.getMessages(chatId)

    override suspend fun openWSConnection() {
        remoteDataSource.openWSConnection()
    }

    override suspend fun closeConnection() {
        remoteDataSource.closeConnection()
    }

    override fun getSocketConnectionState(): Flow<Boolean> = remoteDataSource.getSocketConnectionState()

    override suspend fun sendMessage(chatId: Int, messageText: String) {
        val lastIndex = localDataSource.getLastMessageId(chatId)
        val message = MessageModel(
            updatedAt = Calendar.getInstance().timeInMillis,
            id = lastIndex + 1,
            textBody = messageText,
            senderId = 1, // Should get from system
            isRead = false,
            loaded = false,
            chatId = chatId
        )
        localDataSource.saveMessages(listOf(message))
        remoteDataSource.sendMessage(chatId, message)
    }

    override suspend fun editMessage(chatId: Int, messageId: Int, newMessageText: String) {
        val editedMessage = localDataSource.getMessageById(chatId, messageId)
            .copy(textBody = newMessageText, loaded = false)
        localDataSource.updateMessage(editedMessage)
        remoteDataSource.editMessage(chatId, editedMessage)
    }

    override suspend fun deleteMessage(chatId: Int, messageId: Int) {
        remoteDataSource.deleteMessage(chatId, messageId)
    }

    override suspend fun readMessage(chatId: Int, messageId: Int) {
        remoteDataSource.readMessage(chatId, messageId)
    }

    private companion object {
        const val MOCK_USER_ID = 1
        const val MOCK_PARTNER_ID = 2
    }
}