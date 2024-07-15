package com.w3dev.chatclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel = viewModel<LoginViewModel>()
    if (loginViewModel.isLoggedIn.collectAsState().value) {
        navController.navigate(NavDestinations.HOME_SCREEN)
    }

    val emailText = loginViewModel.signInCredentials.collectAsState().value.email
    val passwordText = loginViewModel.signInCredentials.collectAsState().value.password
    val areCredentialsError = loginViewModel.areCredentialsValid.collectAsState().value == false
    val isLoginButtonEnabled = loginViewModel.areCredentialsValid.collectAsState().value == true

    Surface {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ElevatedCard(
                    Modifier.wrapContentSize(),
                ) {
                    Column(Modifier.padding(12.dp)) {
                        EmailTextBox(text = emailText, areCredentialsError) {
                            loginViewModel.validateAndSetEmailText(it)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LoginPasswordTextBox(text = passwordText, areCredentialsError) {
                            loginViewModel.validateAndSetPasswordText(it)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        loginViewModel.signIn()
                    }, enabled = isLoginButtonEnabled, shape = ButtonDefaults.filledTonalShape, colors = ButtonDefaults.filledTonalButtonColors(), modifier = Modifier.width(
                        TextFieldDefaults.MinWidth
                    )
                ) {
                    Text("Login")
                }
                TextButton(onClick = { navController.navigate(NavDestinations.SIGNUP_SCREEN) }) {
                    Text(text = "Create an account")
                }
            }
        }
    }
}


@Composable
fun EmailTextBox(
    text: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) }

    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.widthIn(TextFieldDefaults.MinWidth, TextFieldDefaults.MinWidth),
        singleLine = true,
        keyboardActions = keyboardActions,
        placeholder = { Text("Email") },
        label = { Text("Email") },
        isError = isError
    )
}

@Composable
fun LoginPasswordTextBox(
    text: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardActions = KeyboardActions { keyboardController?.hide() }

    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.widthIn(TextFieldDefaults.MinWidth, TextFieldDefaults.MinWidth),
        singleLine = true,
        keyboardActions = keyboardActions,
        visualTransformation = PasswordVisualTransformation(),
        placeholder = { Text("Password") },
        label = { Text("Password") },
        isError = isError
    )
}