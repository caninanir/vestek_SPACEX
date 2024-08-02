package com.can_inanir.spacex.ui.main

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitialAuthViewModel @Inject constructor(
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}