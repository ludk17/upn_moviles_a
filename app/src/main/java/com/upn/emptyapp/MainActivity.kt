package com.upn.emptyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.upn.emptyapp.ui.screens.FirebaseFormScreen
import com.upn.emptyapp.ui.screens.ListaFirebaseScreen
import com.upn.emptyapp.ui.screens.LoginScreen
import com.upn.emptyapp.ui.screens.pages.LoginPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val auth = Firebase.auth
            NavHost(
                navController = navController,
                startDestination = if (auth.currentUser == null) "login" else "home"
            ) {
                composable("login") {
                    LoginScreen(navController)
                }
                composable("home") {
                    ListaFirebaseScreen(navController)
                }
                composable("crear_estudiante") {
                    FirebaseFormScreen(navController)
                }
            }

        }
    }
}