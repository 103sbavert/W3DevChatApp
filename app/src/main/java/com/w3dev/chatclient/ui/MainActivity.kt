package com.w3dev.chatclient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.w3dev.chatclient.others.NavDestinations
import com.w3dev.chatclient.ui.screens.ChatScreen
import com.w3dev.chatclient.ui.screens.HomeScreen
import com.w3dev.chatclient.ui.screens.LoginScreen
import com.w3dev.chatclient.ui.screens.NewChatScreen
import com.w3dev.chatclient.ui.screens.SignUpScreen
import com.w3dev.chatclient.ui.theme.W3DevChatClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            W3DevChatClientTheme {
                Surface {
                    NavHost(navController = navController, startDestination = NavDestinations.LOGIN_SCREEN) {
                        composable(NavDestinations.LOGIN_SCREEN) {
                            LoginScreen(navController)
                        }

                        composable(NavDestinations.HOME_SCREEN) {
                            HomeScreen(navController)
                        }

                        composable(NavDestinations.SIGNUP_SCREEN) {
                            SignUpScreen(navController)
                        }

                        composable(NavDestinations.NEW_CHAT_SCREEN) {
                            NewChatScreen(navController = navController)
                        }

                        composable(NavDestinations.CHAT_SCREEN + "/{contactId}") {
                            val id = it.arguments?.getString("contactId")
                            ChatScreen(navController, id!!)
                        }
                    }
                }
            }
        }
    }
}