package com.w3dev.chatclient.data.models

data class SignInCredentials(
    val email: String,
    val password: String
)

data class SignUpCredentials(
    val name: String,
    val email: String,
    val password: String
)
