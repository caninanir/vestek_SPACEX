package com.caninanir.spacex.presentation.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.caninanir.spacex.presentation.ui.features.splashscreen.SplashScreen
import com.caninanir.spacex.presentation.viewmodel.InitialAuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SpaceXMainApp() {
    val authViewModel: InitialAuthViewModel = hiltViewModel()
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }
    val navController = rememberNavController() // Create NavController here

    val googleSignInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        authViewModel.handleGoogleSignInResult(
            result = result,
            context = context,
            navController = navController
        )
    }

    LaunchedEffect(Unit) {
        authViewModel.registerGoogleSignInLauncher(googleSignInLauncher)
        delay(timeMillis = 500)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        NavGraph(navController, signInWithGoogle = { authViewModel.signInWithGoogle() })
    }
}
