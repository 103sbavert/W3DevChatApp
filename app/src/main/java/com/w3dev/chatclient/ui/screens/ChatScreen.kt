package com.w3dev.chatclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.w3dev.chatclient.data.models.Message
import com.w3dev.chatclient.data.models.SenderType
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.viewmodels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, contactId: String) {
    val chatViewModel = viewModel<ChatViewModel>(factory = ChatViewModel.factory(contactId))
    val contact by chatViewModel.contact.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { contact?.name }, navigationIcon = { IconButton(onClick = { navController.navigate(NavDestinations.HOME_SCREEN) }) { Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back arrow") } })
    }) { contentPadding ->
        Column {
            LazyColumn(
                Modifier
                    .padding(contentPadding)
                    .weight(1F)
            ) {
                contact?.let {
                    items(contact!!.messages.size) { index ->
                        val message = contact!!.messages[index]
                        when (message.senderType) {
                            SenderType.SELF -> ReceivedMessage(message = message)
                            SenderType.CONTACT -> SentMessage(message = message)
                        }
                    }
                }
            }
            MessageTextField(
                modifier = Modifier.height(52.dp),
                text = chatViewModel.newMessageText.collectAsState().value,
                onValueChange = { chatViewModel.updateMessageText(it) },
                onSend = { chatViewModel.sendMessage() },
                isSendButtonEnabled = chatViewModel.isSendButtonEnabled.collectAsState().value
            )
        }
    }

}

@Composable
fun MessageTextField(
    modifier: Modifier, text: String, onValueChange: (String) -> Unit, onSend: () -> Unit, isSendButtonEnabled: Boolean
) {
    Row(modifier.fillMaxWidth()) {
        TextField(value = text, onValueChange = onValueChange, Modifier.weight(1F))
        IconButton(onClick = { onSend() }, enabled = isSendButtonEnabled) {
            Icon(Icons.AutoMirrored.Default.Send, contentDescription = "Send button")
        }
    }
}


@Composable
fun ReceivedMessage(message: Message) {
    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ElevatedCard {
            Column {
                Text(text = message.text, modifier = Modifier.align(Alignment.Start))
                Text(text = message.text, modifier = Modifier.align(Alignment.End), style = MaterialTheme.typography.labelSmall)
            }

        }
    }
}


@Composable
fun SentMessage(message: Message) {
    Box(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ElevatedCard {
            Column {
                Text(text = message.text, modifier = Modifier.align(Alignment.End))
                Text(text = message.text, modifier = Modifier.align(Alignment.Start), style = MaterialTheme.typography.labelSmall)
            }

        }
    }
}