package com.w3dev.chatclient.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.w3dev.chatclient.data.UserUtil
import com.w3dev.chatclient.data.models.Contact
import com.w3dev.chatclient.data.models.Message
import com.w3dev.chatclient.data.models.SenderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(contactId: String) : ViewModel() {

    companion object {
        fun factory(contactId: String) = viewModelFactory {
            this.addInitializer(ChatViewModel::class) { ChatViewModel(contactId) }
        }
    }

    private val userUtil = UserUtil()
    private val _contact = MutableStateFlow<Contact?>(null)
    val contact: StateFlow<Contact?>
        get() = _contact

    private val _newMessageText = MutableStateFlow("")
    val newMessageText: StateFlow<String>
        get() = _newMessageText

    private val _isSendButtonEnabled = MutableStateFlow(newMessageText.value.isEmpty())
    val isSendButtonEnabled: StateFlow<Boolean>
        get() = _isSendButtonEnabled


    fun updateMessageText(messageText: String) {
        _newMessageText.tryEmit(messageText)
        _isSendButtonEnabled.tryEmit(messageText.isNotEmpty() && messageText.isNotBlank())
    }

    fun sendMessage() {
        contact.value?.let { userUtil.messagesComponent?.sendMessage(it, Message(newMessageText.value, System.currentTimeMillis().toString(), SenderType.SELF)) }
    }

    init {
        viewModelScope.launch {
            userUtil.messagesComponent?.contacts?.collect {
                if (it.id == contactId) _contact.emit(it)
            }
        }
    }
}
