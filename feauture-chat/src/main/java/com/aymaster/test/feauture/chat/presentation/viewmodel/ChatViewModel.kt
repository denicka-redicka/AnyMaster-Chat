package com.aymaster.test.feauture.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.repository.api.data.ChatRepository
import com.aymaster.test.core.repository.api.di.ChatRepositoryApi
import com.aymaster.test.feauture.chat.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel(
    private val repository: ChatRepository<MessageModel>
) : ViewModel() {

    private var headerState: HeaderState = HeaderState("", false, 0, false)

    private val viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Empty)
    private val chatHeaderStateFlow: MutableStateFlow<HeaderState> = MutableStateFlow(headerState)
    private var messagesFlow: Flow<List<MessageModel>> = flowOf()

    internal fun getViewState(): Flow<ViewState> = viewState
    internal fun getHeaderState(): Flow<HeaderState> = chatHeaderStateFlow
    fun subscribeMessages(): Flow<List<MessageModel>> = messagesFlow

    init {
        viewModelScope.launch {
            (repository.getSocketConnectionState() as Flow<Boolean>).collect { connected ->
                headerState = headerState.copy(connected = connected)
                chatHeaderStateFlow.value = headerState
            }
        }
    }

    fun openWSConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.openWSConnection()
        }
    }

    fun closeWSConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.closeConnection()
        }
    }

    fun initData(chatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            //here should be request in DB for that data
            headerState = HeaderState("Daniel Moris", true, R.drawable.chat_img, false)
            chatHeaderStateFlow.value = headerState
            messagesFlow = repository.getAllMessages(chatId)
        }
    }

    fun readMessage(chatId: Int, messageId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.readMessage(chatId, messageId)
        }
    }

    fun selectMessage(message: MessageModel) {
        viewState.value = ViewState.MessageSelected(message)
    }

    fun sendMessage(chatId: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessage(chatId, text)
        }
    }

    fun sendEditMessage(messageId: Int, chatId: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editMessage(chatId = chatId, messageId = messageId, newMessageText = text)
            viewState.value = ViewState.Empty
        }
    }

    fun onEditMessageClick(message: MessageModel) {
        viewState.value = ViewState.EditMessage(message)
    }

    fun deleteMessage(chatId: Int, messageId: Int) {
        viewModelScope.launch {
            repository.deleteMessage(chatId, messageId)
            viewState.value = ViewState.Empty
        }
    }

    fun showEmptyState() {
        viewState.value = ViewState.Empty
    }

    internal data class HeaderState(
        val chatName: String,
        val verified: Boolean,
        val chatImgRes: Int,
        val connected: Boolean,
    )

    internal sealed interface ViewState {
        object Empty : ViewState

        data class MessageSelected(val editedMessage: MessageModel) : ViewState

        data class EditMessage(val editedMessage: MessageModel) : ViewState
    }

    @Suppress("UNCHECKED_CAST")
    class MoviesVmFactory @Inject constructor(
        private val repositoryApi: ChatRepositoryApi<@JvmSuppressWildcards MessageModel>
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(repositoryApi.getChatRepository()) as T
        }
    }
}