package com.can_inanir.spacex.ui.main

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.can_inanir.spacex.ui.feature.login.AuthViewModel
import com.can_inanir.spacex.ui.feature.splashscreen.SplashScreen
import kotlinx.coroutines.delay

@SuppressLint("RememberReturnType")
@Composable
fun MyApp(signInWithGoogle: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }

    remember { (context as? MainActivity)?.authViewModel = authViewModel }

    LaunchedEffect(Unit) {
        delay(timeMillis = 2000) // Show SplashScreen for 2 seconds
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        NavGraph(signInWithGoogle = signInWithGoogle)
    }
}