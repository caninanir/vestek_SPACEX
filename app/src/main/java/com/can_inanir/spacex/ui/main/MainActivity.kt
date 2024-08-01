@file:Suppress("DEPRECATION")

package com.can_inanir.spacex.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.can_inanir.spacex.R
import com.can_inanir.spacex.ui.feature.login.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var authViewModel: AuthViewModel

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
        account?.idToken?.let { authViewModel.handleGoogleAccessToken(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            MyApp { signInWithGoogle() }
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
        delay(timeMillis = 200)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        NavGraph(signInWithGoogle = signInWithGoogle)
    }
}
