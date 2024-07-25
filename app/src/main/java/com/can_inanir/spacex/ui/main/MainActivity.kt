@file:Suppress("DEPRECATION")

package com.can_inanir.spacex.ui.main


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.can_inanir.spacex.R
import com.can_inanir.spacex.ui.feature.login.AuthViewModel

import com.google.android.gms.auth.api.signin.GoogleSignIn

import kotlinx.coroutines.delay


import androidx.core.view.WindowCompat

import androidx.compose.ui.layout.*
import androidx.compose.ui.graphics.Color.*
import androidx.compose.ui.graphics.toArgb

import com.google.android.gms.auth.api.signin.GoogleSignInClient


@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var authViewModel: AuthViewModel

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
        account?.idToken?.let { authViewModel.handleGoogleAccessToken(it) }
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        //or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()














        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        setContent {
            MyApp {
                authViewModel = ViewModelProvider(this@MainActivity)[AuthViewModel::class.java]
                signInWithGoogle()
            }
        }


    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
}

@Composable
fun SplashScreen() {
    Image(
        painter = painterResource(id = R.drawable.space_x_android_splash_screen_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@SuppressLint("RememberReturnType")
@Composable
fun MyApp(signInWithGoogle: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }


    remember { (context as? MainActivity)?.authViewModel = authViewModel }

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
    }


    remember { (context as? MainActivity)?.authViewModel = authViewModel }

    if (showSplash) {
        SplashScreen()
    } else {
        NavGraph(signInWithGoogle = signInWithGoogle)
    }
}

