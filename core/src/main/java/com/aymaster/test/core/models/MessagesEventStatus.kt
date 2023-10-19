package com.aymaster.test.core.models

enum class MessagesEventStatus {
    NEW_MESSAGE_TYPE,
    DELETE_MESSAGE_TYPE,
    EDIT_MESSAGE_TYPE,
    MESSAGE_READ_TYPE,
    UNDEFINED;

    companion object {
        fun fromInt(status: Int): MessagesEventStatus = when(status) {
            1 -> NEW_MESSAGE_TYPE
            2 -> DELETE_MESSAGE_TYPE
            3 -> EDIT_MESSAGE_TYPE
            4 -> MESSAGE_READ_TYPE
            else -> UNDEFINED
        }
    }

}

fun MessagesEventStatus.toInt() : Int = when (this) {
    MessagesEventStatus.NEW_MESSAGE_TYPE -> 1
    MessagesEventStatus.DELETE_MESSAGE_TYPE -> 2
    MessagesEventStatus.EDIT_MESSAGE_TYPE -> 3
    MessagesEventStatus.MESSAGE_READ_TYPE -> 4
    MessagesEventStatus.UNDEFINED -> 0
}