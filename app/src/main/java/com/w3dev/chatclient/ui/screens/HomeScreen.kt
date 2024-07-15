package com.w3dev.chatclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.w3dev.chatclient.R
import com.w3dev.chatclient.data.models.Contact
import com.w3dev.chatclient.data.models.User
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.toCollection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeLoginViewModel = viewModel<HomeViewModel>()
    val contacts: SnapshotStateList<Contact?> = remember {
        mutableStateListOf()
    }

    if (!homeLoginViewModel.isLoggedIn.collectAsState().value) navController.navigate(NavDestinations.LOGIN_SCREEN)

    LaunchedEffect(Unit) {
        homeLoginViewModel.contacts.toCollection(contacts)
    }


    Surface {
        Scaffold(floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                navController.navigate(NavDestinations.NEW_CHAT_SCREEN)
            }) {
                Text(text = "New Chat")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Add, contentDescription = "New chat button")
            }
        }, topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }, actions = {
                IconButton(onClick = { homeLoginViewModel.logout() }) {
                    Icon(Icons.AutoMirrored.Default.Logout, contentDescription = "Log out button")
                }
            })
        }) { contentPadding ->
            LazyColumn(Modifier.padding(contentPadding)) {
                items(contacts.size) { index ->
                    contacts[index]?.let { contact ->
                        ContactItem(contact) {
                            navController.navigate(NavDestinations.CHAT_SCREEN + "/${contact.id}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ContactItem(contact: Contact, onContactClicked: (User) -> Unit) {
    val lastMessage = contact.messages.lastOrNull()

    ElevatedCard(
        onClick = { onContactClicked(contact) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                if (lastMessage != null) Text(text = lastMessage.text, style = MaterialTheme.typography.bodySmall) else Text(
                    text = "Send a message", style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Light
                )
            }
            if (lastMessage != null) Text(text = lastMessage.time, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.typography.labelSmall.color.copy(alpha = 0.4F)))
        }
    }
}
