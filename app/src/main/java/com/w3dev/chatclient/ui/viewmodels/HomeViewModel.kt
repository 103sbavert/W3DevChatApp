package com.w3dev.chatclient.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.w3dev.chatclient.data.UserUtil

class HomeViewModel : ViewModel() {
    private val userUtil = UserUtil()
    val isLoggedIn = userUtil.isUserLoggedIn

    val contacts = userUtil.messagesComponent?.contacts ?: throw IllegalStateException("User shouldn't be logged in, or there's something else")

    fun logout() {
        userUtil.logout()
    }
}
