package com.w3dev.chatclient.data.models

enum class SenderType {
    SELF, CONTACT
}

open class User(
    val id: String,
    val name: String,
    val emailAddress: String,
) {

    override fun equals(other: Any?): Boolean {
        if (other !is User) return false
        return id == other.id && emailAddress == other.emailAddress
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + emailAddress.hashCode()
        return result
    }
}

class CurrentUser(id: String, name: String, emailAddress: String, val contacts: Map<String, Contact>) : User(id, name, emailAddress)

class Contact(
    id: String, name: String, emailAddress: String, val messages: List<Message>
) : User(id, name, emailAddress)

data class Message(
    val text: String, val time: String, val senderType: SenderType
)

