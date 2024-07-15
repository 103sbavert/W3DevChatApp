package com.w3dev.chatclient.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEvent
import com.google.firebase.database.childEvents
import com.google.firebase.database.database
import com.w3dev.chatclient.data.models.Contact
import com.w3dev.chatclient.data.models.Message
import com.w3dev.chatclient.data.models.SenderType
import com.w3dev.chatclient.data.models.SignInCredentials
import com.w3dev.chatclient.data.models.SignUpCredentials
import com.w3dev.chatclient.data.models.User
import com.w3dev.chatclient.others.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "UserUtil"

class UserUtil {
    private val auth = Firebase.auth
    private val _isUserLoggedIn = MutableStateFlow(auth.currentUser != null)
    private val allUsersDb = Firebase.database.getReference("/users/")

    val isUserLoggedIn: StateFlow<Boolean>
        get() = _isUserLoggedIn

    val messagesComponent: MessagesComponent?
        get() = if (auth.currentUser != null) MessagesComponent(auth.currentUser!!.uid) else null

    fun login(credentials: SignInCredentials) {
        val task = auth.signInWithEmailAndPassword(credentials.email, credentials.password)

        task.addOnCompleteListener {
            _isUserLoggedIn.tryEmit(auth.currentUser != null)
        }
    }

    fun signup(signUpCredentials: SignUpCredentials) {
        auth.createUserWithEmailAndPassword(signUpCredentials.email, signUpCredentials.password).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = it.result.user?.uid ?: throw NullPointerException("Weird!")
                allUsersDb.child(uid).setValue(User(uid, signUpCredentials.name, signUpCredentials.email))
                _isUserLoggedIn.tryEmit(true)
            }
        }
    }

    fun logout() {
        auth.signOut()
        _isUserLoggedIn.tryEmit(false)
    }

    class MessagesComponent(uid: String) {
        private val dbObj = Firebase.database(Constants.REALTIME_DB_URL)
        private val allUsersDb = dbObj.getReference("/users/")
        private val localUserDb = allUsersDb.child("/$uid/")
        private val contactsDb = localUserDb.child("/contacts/")
        val contacts = contactsDb.childEvents.map { childEvent ->
            when (childEvent) {
                is ChildEvent.Added -> {
                    childEvent.snapshot.let { snapshot ->
                        Contact(snapshot.child("id").value.toString(),
                            snapshot.child("name").value.toString(),
                            snapshot.child("emailAddress").value.toString(),
                            snapshot.child("messages").children.map { Message(it.child("text").value as String, it.child("time").value as String, SenderType.valueOf(it.child("senderType").value as String)) })
                    }

                }

                is ChildEvent.Changed -> {
                    childEvent.snapshot.let { snapshot ->
                        Contact(snapshot.child("id").value.toString(),
                            snapshot.child("name").value.toString(),
                            snapshot.child("emailAddress").value.toString(),
                            snapshot.child("messages").children.map { Message(it.child("text").value as String, it.child("time").value as String, SenderType.valueOf(it.child("senderType").value as String)) })

                    }
                }

                is ChildEvent.Removed -> {
                    childEvent.snapshot.let { snapshot ->
                        Contact(snapshot.child("id").value.toString(),
                            snapshot.child("name").value.toString(),
                            snapshot.child("emailAddress").value.toString(),
                            snapshot.child("messages").children.map { Message(it.child("text").value as String, it.child("time").value as String, SenderType.valueOf(it.child("senderType").value as String)) })
                    }
                }

                is ChildEvent.Moved -> {
                    childEvent.snapshot.let { snapshot ->
                        Contact(snapshot.child("id").value.toString(),
                            snapshot.child("name").value.toString(),
                            snapshot.child("emailAddress").value.toString(),
                            snapshot.child("messages").children.map { Message(it.child("text").value as String, it.child("time").value as String, SenderType.valueOf(it.child("senderType").value as String)) })
                    }
                }
            }
        }

        fun addNewContact(newContact: User) {
            contactsDb.child(newContact.id).setValue(Contact(newContact.id, newContact.name, newContact.emailAddress, emptyList()))
        }

        val newContacts = callbackFlow<List<User>> {
            val temp = mutableListOf<User>()
            Log.e(TAG, "COLLECT CONTACTS")

            val contactsJob = launch {
                contactsDb.childEvents.collect { childEvent ->
                    when (childEvent) {
                        is ChildEvent.Added -> {
                            val contact = childEvent.snapshot.let { snapshot ->
                                Contact(snapshot.child("id").value.toString(),
                                    snapshot.child("name").value.toString(),
                                    snapshot.child("emailAddress").value.toString(),
                                    snapshot.child("messages").children.map { Message(it.child("text").value as String, it.child("time").value as String, SenderType.valueOf(it.child("senderType").value as String)) })

                            }
                            temp.add(contact)
                        }

                        is ChildEvent.Changed -> {

                        }

                        is ChildEvent.Moved -> {

                        }

                        is ChildEvent.Removed -> {

                        }
                    }
                }

            }

            Log.e(TAG, "COLLECT ALL USERS")


            val allUsersJob = launch {
                allUsersDb.childEvents.collect { childEvent ->
                    when (childEvent) {
                        is ChildEvent.Added -> {
                            val user = childEvent.snapshot.let { snapshot ->
                                Contact(snapshot.child("id").value.toString(), snapshot.child("name").value.toString(), snapshot.child("emailAddress").value.toString(), snapshot.child("messages").children.map {
                                    (it.value as Message)
                                })
                            }
                            temp.add(user)
                        }

                        is ChildEvent.Changed -> {

                        }

                        is ChildEvent.Moved -> {

                        }

                        is ChildEvent.Removed -> {

                        }
                    }
                }

            }

            awaitClose {
                contactsJob.cancel()
                allUsersJob.cancel()
                channel.close()
            }
        }

        fun sendMessage(contact: Contact, message: Message) {
            contactsDb.child(contact.id).child("messages").push().setValue(message)
        }
    }
}