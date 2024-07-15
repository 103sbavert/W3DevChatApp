package com.w3dev.chatclient.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.w3dev.chatclient.data.models.User
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.viewmodels.NewChatViewModel


@Composable
fun NewChatScreen(navController: NavController) {
    val newChatViewModel = viewModel<NewChatViewModel>()
    var newContacts by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    LaunchedEffect(Unit) {
        newChatViewModel.newContacts?.collect { newList ->
            newContacts = newList
        }
    }

    Surface {
        LazyColumn(Modifier.fillMaxSize()) {
            items(newContacts.size) { index ->
                newContacts[index].let { newContact ->
                    NewChatItem(newContact) {
                        newChatViewModel.addContact(newContact)
                        navController.navigate(NavDestinations.CHAT_SCREEN + "/${newContact.id}")
                    }
                }
            }
        }
    }

}

@Composable
fun NewChatItem(newContact: User, onContactClicked: (User) -> Unit) {
    ElevatedCard(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = newContact.name, style = MaterialTheme.typography.titleMedium)
            }

            IconButton(onClick = { onContactClicked(newContact) }) {
                Icon(Icons.AutoMirrored.Default.OpenInNew, contentDescription = "Open chat icon")
            }
        }
    }
}
