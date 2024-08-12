package com.caninanir.spacex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _userState: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState

    private val _loginErrorState = MutableStateFlow<String?>(null)
    val loginErrorState: StateFlow<String?> = _loginErrorState

    init {
        auth.addAuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let { user ->
                viewModelScope.launch {
                    checkAndCreateUserDocument(user.email!!)
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                if (user != null) {
                    _userState.value = user
                    checkAndCreateUserDocument(user.email!!)
                    _loginErrorState.value = null
                } else {
                    _loginErrorState.value = "Your password or e-mail address is wrong."
                }
            } catch (e: Exception) {
                Timber.log(1, e.message.toString())
                _loginErrorState.value = "Your password or e-mail address is wrong."
            }
        }
    }

    fun logout() {
        auth.signOut()
        _userState.value = null
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
               if (user != null && user.email != null) {
                    _userState.value = user
                    checkAndCreateUserDocument(user.email!!)
                   _loginErrorState.value = null
               }
            } catch (e: FirebaseAuthUserCollisionException) {
                Timber.log(1, e.message.toString())
                _loginErrorState.value = "The email address is already in use by another account."
            } catch (e: Exception) {
                Timber.log(1, e.message.toString())
                _loginErrorState.value = "Account creation failed: $e"
            }
        }
    }

    private suspend fun checkAndCreateUserDocument(email: String) {
        val userDocRef = db.collection("users").document(email)
        val documentSnapshot = userDocRef.get().await()
        if (!documentSnapshot.exists()) {
            val userData = hashMapOf(
                "email" to email,
                "favorites" to emptyMap<String, Boolean>()
            )
            userDocRef.set(userData, SetOptions.merge()).await()
        }
    }
}