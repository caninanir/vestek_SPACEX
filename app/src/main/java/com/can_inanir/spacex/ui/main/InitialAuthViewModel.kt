package com.can_inanir.spacex.ui.main

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialAuthViewModel @Inject constructor(
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    fun initializeGoogleSignInLauncher(context: Context) {
        googleSignInLauncher = (context as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result?.data)
            val account: GoogleSignInAccount? = task?.getResult(ApiException::class.java)
            account?.idToken?.let {
                handleGoogleAccessToken(it)
            }
        }
    }

    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun handleGoogleAccessToken(token: String) {
        viewModelScope.launch {
            // Handle the token
        }
    }
}