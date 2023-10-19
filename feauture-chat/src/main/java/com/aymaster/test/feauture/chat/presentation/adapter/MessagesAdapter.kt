package com.aymaster.test.feauture.chat.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aymaster.test.core.models.MessageModel
import com.aymaster.test.feauture.chat.R
import java.text.SimpleDateFormat
import java.util.Date

internal class MessagesAdapter(
    private val userId: Int,
    private val onLongTap: (MessageModel) -> Unit,
    private val onOptionMenuDismissed: () -> Unit,
    private val readMessage: (Int) -> Unit,
    private val onOptionEditSelected: (MessageModel) -> Unit,
    private val onOptionDeleteSelected: (Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private var messages: MutableList<MessageModel> = mutableListOf()

    fun setMessagesList(messages: List<MessageModel>) {
        this.messages = messages.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.holder_outgoing_message -> OutgoingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.holder_outgoing_message, parent, false)
            )
            R.layout.holder_incoming_message -> IncomingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.holder_incoming_message, parent, false)
            )
            R.layout.holder_offer -> OfferViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.holder_offer, parent, false)
            )
            else -> throw IllegalArgumentException("Illegal type: $viewType")
        }
    }

    override fun getItemCount(): Int = messages.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is OutgoingViewHolder -> holder.bind(messages[position])
            is IncomingViewHolder -> holder.bind(messages[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < messages.size && messages[position].senderId == userId -> R.layout.holder_outgoing_message
            position < messages.size && messages[position].senderId != userId -> R.layout.holder_incoming_message
            else -> R.layout.holder_offer
        }
    }

    private fun handleLongClick(context: Context, view: View, message: MessageModel) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(
            if (message.senderId == userId)
                R.menu.outgoing_message_options_menu
            else
                R.menu.incoming_message_options_menu
        )
        popupMenu.setOnMenuItemClickListener(object : OnMenuItemClickListener, PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.optionCopy -> {
                        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.optionDelete -> {
                        onOptionDeleteSelected(message.id)
                        true
                    }
                    R.id.optionEdit -> {
                        onOptionEditSelected(message)
                        true
                    }
                    else -> false
                }
            }
        })
        popupMenu.setOnDismissListener { view.elevation = 0f }
        view.elevation
        popupMenu.show()
    }

    inner class IncomingViewHolder(private val view: View) : ViewHolder(view) {
        private val messageText = view.findViewById<TextView>(R.id.messageText)
        private val sentDate = view.findViewById<TextView>(R.id.sentAt)
        private val bubble = view.findViewById<ConstraintLayout>(R.id.messageBubble)

        fun bind(message: MessageModel) {
            messageText.text = message.textBody
            sentDate.text = SimpleDateFormat("HH:mm").format(Date(message.updatedAt))

            bubble.setOnLongClickListener {
                handleLongClick(context = view.context, view = bubble, message = message)
                onLongTap(message)
                return@setOnLongClickListener true
            }
            view.doOnAttach {
                if (!message.isRead) readMessage.invoke(message.id)
            }
        }
    }

    inner class OutgoingViewHolder(private val view: View) : ViewHolder(view) {
        private val messageText = view.findViewById<TextView>(R.id.messageText)
        private val sentDate = view.findViewById<TextView>(R.id.sentAt)
        private val readStatus = view.findViewById<AppCompatImageView>(R.id.readStatus)
        private val bubble = view.findViewById<ConstraintLayout>(R.id.messageBubble)

        fun bind(message: MessageModel) {
            messageText.text = message.textBody
            sentDate.text = SimpleDateFormat("HH:mm").format(Date(message.updatedAt))
            readStatus.background = AppCompatResources.getDrawable(
                view.context,
                when {
                    !message.loaded -> R.drawable.loading_watch
                    message.isRead -> R.drawable.read_status
                    else -> R.drawable.not_read_status
                }
            )

            bubble.setOnLongClickListener {
                handleLongClick(context = view.context, view = bubble, message = message)
                onLongTap(message)
                return@setOnLongClickListener true
            }
        }
    }

    class OfferViewHolder(view: View) : ViewHolder(view) {

    }

    private class MessageDiffCallBack : DiffUtil.ItemCallback<MessageModel>() {
        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =
            oldItem == newItem
    }
}

