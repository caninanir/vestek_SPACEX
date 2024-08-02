package com.can_inanir.spacex.ui.main

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.can_inanir.spacex.ui.feature.splashscreen.SplashScreen
import kotlinx.coroutines.delay

@SuppressLint("RememberReturnType")
@Composable
fun SpaceXMainApp() {
    val authViewModel: InitialAuthViewModel = hiltViewModel()
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(timeMillis = 2000) // Show splash screen for 2 seconds
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        NavGraph(signInWithGoogle = { authViewModel.signInWithGoogle() })
    }
}