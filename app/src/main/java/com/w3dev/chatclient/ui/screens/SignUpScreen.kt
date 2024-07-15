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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.viewmodels.SignUpViewModel

@Composable
fun SignUpPasswordTextBox(
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
        placeholder = { Text("Password") },
        label = { Text("Password") },
        isError = isError
    )
}


@Composable
fun NameTextBox(
    text: String,
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
        placeholder = { Text("Name") },
        label = { Text("Name") },
    )
}


@Composable
fun SignUpScreen(navController: NavController) {
    val signUpViewModel = viewModel<SignUpViewModel>()
    if (signUpViewModel.isLoggedIn.collectAsState().value) {
        navController.navigate(NavDestinations.HOME_SCREEN)
    }

    val nameText = signUpViewModel.signUpCredentials.collectAsState().value.name
    val emailText = signUpViewModel.signUpCredentials.collectAsState().value.email
    val passwordText = signUpViewModel.signUpCredentials.collectAsState().value.password
    val areCredentialsError = signUpViewModel.areCredentialsValid.collectAsState().value == false
    val isSignUpButtonEnabled = signUpViewModel.areCredentialsValid.collectAsState().value == true

    Surface {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ElevatedCard(
                    Modifier.wrapContentSize(),
                ) {
                    Column(Modifier.padding(12.dp)) {
                        NameTextBox(text = nameText) {
                            signUpViewModel.setNameText(it)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        EmailTextBox(text = emailText, areCredentialsError) {
                            signUpViewModel.validateAndSetEmailText(it)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        SignUpPasswordTextBox(text = passwordText, areCredentialsError) {
                            signUpViewModel.validateAndSetPasswordText(it)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        signUpViewModel.signUp()
                    }, enabled = isSignUpButtonEnabled, shape = ButtonDefaults.filledTonalShape, colors = ButtonDefaults.filledTonalButtonColors(), modifier = Modifier.width(TextFieldDefaults.MinWidth)
                ) {
                    Text("SignUp")
                }
                TextButton(onClick = { navController.navigate(NavDestinations.LOGIN_SCREEN) }) {
                    Text(text = "Back to Login")
                }
            }
        }
    }
}