package com.aymaster.test.feauture.chat.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.core.utils.observeWithLifecycle
import com.aymaster.test.feauture.chat.R
import com.aymaster.test.feauture.chat.di.ChatComponentViewModel
import com.aymaster.test.feauture.chat.presentation.adapter.MessagesAdapter
import com.aymaster.test.feauture.chat.presentation.utils.BlurBuilder
import com.aymaster.test.feauture.chat.presentation.viewmodel.ChatViewModel
import dagger.Lazy
import javax.inject.Inject

class ChatFragment : Fragment(R.layout.fargment_chat) {
    @Inject
    lateinit var moviesViewModelFactory: Lazy<ChatViewModel.MoviesVmFactory>

    private val viewModel: ChatViewModel by viewModels {
        moviesViewModelFactory.get()
    }
    private val componentViewModel: ChatComponentViewModel by viewModels()
    private var chatId: Int = -1

    //TODO change to something like viewBinding
    private var editText: EditText? = null
    private var sendButton: ImageButton? = null
    private var cancelButton: ImageButton? = null
    private var messagesList: RecyclerView? = null
    private var editableText: TextView? = null
    private var editTextHeader: TextView? = null
    private var blurBackground: ImageView? = null
    private var chatName: TextView? = null
    private var chatImg: ImageView? = null
    private var verifiedStatus: TextView? = null
    private var loadingStatus: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        componentViewModel.contactsListComponent.inject(this)
        chatId = arguments?.getInt(CHAT_ID_KEY, MOCK_CHAT_ID) ?: MOCK_CHAT_ID
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initData(chatId)
        initViewElements(view)

        val adapter = MessagesAdapter(
            userId = 1,
            onLongTap = viewModel::selectMessage,
            readMessage = { messageId -> viewModel.readMessage(chatId, messageId) },
            onOptionMenuDismissed = viewModel::showEmptyState,
            onOptionEditSelected = viewModel::onEditMessageClick,
            onOptionDeleteSelected = { messageId -> viewModel.deleteMessage(chatId, messageId) }
        )
        messagesList?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
        messagesList?.adapter = adapter


        editText?.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                sendButton?.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        )
        blurBackground?.setOnClickListener { viewModel.showEmptyState() }
        cancelButton?.setOnClickListener { viewModel.showEmptyState() }
        observeMessages(adapter)
        observeHeaderState()
        observeViewState()
    }

    override fun onStart() {
        super.onStart()
        viewModel.openWSConnection()
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeWSConnection()
    }

    private fun initViewElements(view: View) {
        messagesList = view.findViewById(R.id.messages)
        editText = view.findViewById(R.id.messagesInput)
        sendButton = view.findViewById(R.id.sendButton)
        cancelButton = view.findViewById(R.id.cancelButton)
        editableText = view.findViewById(R.id.editableMessageText)
        editTextHeader = view.findViewById(R.id.editMessageHeader)
        blurBackground = view.findViewById(R.id.blurBackground)
        chatName = view.findViewById(R.id.chatUserName)
        chatImg = view.findViewById(R.id.avatarImg)
        verifiedStatus = view.findViewById(R.id.verifiedStatus)
        loadingStatus = view.findViewById(R.id.loadingStatus)
    }

    private fun observeMessages(adapter: MessagesAdapter) {
        viewModel.subscribeMessages().observeWithLifecycle(this) { messagesList ->
            adapter.setMessagesList(messagesList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun observeHeaderState() {
        viewModel.getHeaderState().observeWithLifecycle(this) { headerState ->
            if (headerState.chatImgRes != 0) {
                chatImg?.background = AppCompatResources.getDrawable(requireContext(), headerState.chatImgRes)
            }
            chatName?.text = headerState.chatName
            when {
                !headerState.connected -> {
                    loadingStatus?.isVisible = true
                    verifiedStatus?.isVisible = false
                }
                headerState.verified -> {
                    loadingStatus?.isVisible = false
                    verifiedStatus?.isVisible = true
                }
                !headerState.verified -> {
                    loadingStatus?.isVisible = false
                    verifiedStatus?.isVisible = false
                }
            }
        }
    }

    private fun observeViewState() {
        viewModel.getViewState().observeWithLifecycle(this) { state ->
            when (state) {
                is ChatViewModel.ViewState.Empty -> emptyState()
                is ChatViewModel.ViewState.EditMessage -> editMessageState(state.editedMessage)
                is ChatViewModel.ViewState.MessageSelected -> selectedMessageState()
            }
        }
    }

    private fun editMessageState(message: MessageModel) {
        blurBackground?.isVisible = false
        sendButton?.setOnClickListener {
            viewModel.sendEditMessage(messageId = message.id, chatId = chatId, text = editText?.text?.toString() ?: "")
            editText?.text = null
        }
        editTextHeader?.isVisible = true
        editableText?.isVisible = true
        editableText?.text = message.textBody
        cancelButton?.isVisible = true
    }

    private fun emptyState() {
        sendButton?.setOnClickListener {
            viewModel.sendMessage(chatId, editText?.text?.toString() ?: "")
            editText?.text = null
        }
        editTextHeader?.isVisible = false
        editableText?.isVisible = false
        editableText?.text = ""
        cancelButton?.isVisible = false
        blurBackground?.isVisible = false
    }

    private fun selectedMessageState() {
        blurBackground?.isVisible = true
        blurBackground?.setImageBitmap(BlurBuilder.blur(requireContext(), requireView()))
    }

    companion object {
        const val CHAT_ID_KEY = "chat_id_key"
        const val MOCK_CHAT_ID = 123
    }
}