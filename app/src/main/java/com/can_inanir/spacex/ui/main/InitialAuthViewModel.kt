package com.can_inanir.spacex.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.can_inanir.spacex.ui.common.bottomnav.BottomNavItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class InitialAuthViewModel @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val auth: FirebaseAuth
) : ViewModel() {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    fun registerGoogleSignInLauncher(launcher: ActivityResultLauncher<Intent>) {
        googleSignInLauncher = launcher
    }

    fun signInWithGoogle() {
        if (::googleSignInLauncher.isInitialized) {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        } else {
            // Handle the uninitialized state
        }
    }

    fun handleGoogleSignInResult(result: ActivityResult, context: Context, navController: NavHostController) {
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account, context) {
                        navController.navigate(BottomNavItem.Favorites.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount, context: Context, onSuccess: () -> Unit) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithCredential(credential).await()
                if (authResult.user != null) {
                    Toast.makeText(context, "Successfully signed in", Toast.LENGTH_SHORT).show()
                    onSuccess()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Authentication failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}