package com.w3dev.chatclient.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.w3dev.chatclient.data.UserUtil
import com.w3dev.chatclient.data.models.SignInCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"

class LoginViewModel : ViewModel() {
    private val userUtil = UserUtil()

    private val _signInCredentials: MutableStateFlow<SignInCredentials> = MutableStateFlow(SignInCredentials("", ""))
    val signInCredentials: StateFlow<SignInCredentials>
        get() = _signInCredentials

    val isLoggedIn = userUtil.isUserLoggedIn

    private val _areCredentialsValid = MutableStateFlow<Boolean?>(null)
    val areCredentialsValid: StateFlow<Boolean?>
        get() = _areCredentialsValid

    private fun checkCredentialValidation() {
        if (_signInCredentials.value.email.isEmpty() || _signInCredentials.value.password.isEmpty()) {
            _areCredentialsValid.value = null
        } else {
            _areCredentialsValid.value = _signInCredentials.value.email.matches(EMAIL_REGEX.toRegex()) && _signInCredentials.value.password.length >= 8
        }
    }

    fun validateAndSetEmailText(emailText: String) {
        _signInCredentials.value = SignInCredentials(emailText, _signInCredentials.value.password)
        checkCredentialValidation()
    }

    fun validateAndSetPasswordText(passwordText: String) {
        _signInCredentials.value = SignInCredentials(_signInCredentials.value.email, passwordText)
        checkCredentialValidation()
    }

    fun signIn() {
        if (areCredentialsValid.value == true) userUtil.login(signInCredentials.value)
        else throw IllegalStateException("Shouldn't be able to sign in without valid credentials")
    }
}