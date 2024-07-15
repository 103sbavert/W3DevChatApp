package com.w3dev.chatclient.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.w3dev.chatclient.data.UserUtil
import com.w3dev.chatclient.data.models.SignUpCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel : ViewModel() {
    private val userUtil = UserUtil()

    private val _signUpCredentials: MutableStateFlow<SignUpCredentials> = MutableStateFlow(SignUpCredentials("", "", ""))
    val signUpCredentials: StateFlow<SignUpCredentials>
        get() = _signUpCredentials

    val isLoggedIn = userUtil.isUserLoggedIn

    private val _areCredentialsValid = MutableStateFlow<Boolean?>(null)
    val areCredentialsValid: StateFlow<Boolean?>
        get() = _areCredentialsValid

    private fun checkCredentialValidation() {
        if (_signUpCredentials.value.email.isEmpty() || _signUpCredentials.value.password.isEmpty() || signUpCredentials.value.name.isEmpty()) {
            _areCredentialsValid.value = null
        } else {
            _areCredentialsValid.value = _signUpCredentials.value.email.matches(EMAIL_REGEX.toRegex()) && _signUpCredentials.value.password.length >= 8 && _signUpCredentials.value.name.isNotBlank() && _signUpCredentials.value.name.isNotEmpty()
        }
    }

    fun validateAndSetEmailText(emailText: String) {
        _signUpCredentials.value = SignUpCredentials(signUpCredentials.value.name, emailText, _signUpCredentials.value.password)
        checkCredentialValidation()
    }


    fun validateAndSetPasswordText(passwordText: String) {
        _signUpCredentials.value = SignUpCredentials(_signUpCredentials.value.name, _signUpCredentials.value.email, passwordText)
        checkCredentialValidation()
    }

    fun setNameText(nameText: String) {
        _signUpCredentials.value = SignUpCredentials(nameText, _signUpCredentials.value.email, _signUpCredentials.value.password)
        checkCredentialValidation()
    }

    fun signUp() {
        if (areCredentialsValid.value == true) userUtil.signup(signUpCredentials.value)
        else throw IllegalStateException("Shouldn't be able to signup without valid details")
    }
}