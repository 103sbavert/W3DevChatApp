package com.w3dev.chatclient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.theme.W3DevChatClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            W3DevChatClientTheme {
                NavHost(navController = navController, startDestination = NavDestinations.LOGIN_SCREEN) {

                }
            }
        }
    }
}