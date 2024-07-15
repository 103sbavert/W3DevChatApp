package com.w3dev.chatclient.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.w3dev.chatclient.data.UserUtil
import com.w3dev.chatclient.data.models.User

class NewChatViewModel : ViewModel() {
    private val userUtil = UserUtil()

    val newContacts = userUtil.messagesComponent?.newContacts

    fun addContact(newContact: User) {
        userUtil.messagesComponent?.addNewContact(newContact)
    }
}