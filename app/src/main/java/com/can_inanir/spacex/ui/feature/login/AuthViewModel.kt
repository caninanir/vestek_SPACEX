package com.can_inanir.spacex.ui.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _userState: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState

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
                val task = auth.signInWithEmailAndPassword(email, password).await()
                if (task.user != null) {
                    Log.d("AuthViewModel", "signInWithEmail:success")
                    _userState.value = auth.currentUser
                    checkAndCreateUserDocument(auth.currentUser!!.email!!)
                } else {
                    Log.w("AuthViewModel", "signInWithEmail:failure")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "login:failure", e)
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
                val task = auth.createUserWithEmailAndPassword(email, password).await()
                if (task.user != null) {
                    Log.d("AuthViewModel", "createUserWithEmail:success")
                    _userState.value = auth.currentUser
                    checkAndCreateUserDocument(auth.currentUser!!.email!!)
                } else {
                    Log.w("AuthViewModel", "createUserWithEmail:failure")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "createAccount:failure", e)
            }
        }
    }

    fun handleGoogleAccessToken(idToken: String) {
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val task = auth.signInWithCredential(credential).await()
                if (task.user != null) {
                    Log.d("AuthViewModel", "signInWithCredential:success")
                    _userState.value = auth.currentUser
                    checkAndCreateUserDocument(auth.currentUser!!.email!!)
                } else {
                    Log.w("AuthViewModel", "signInWithCredential:failure")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "handleGoogleAccessToken:failure", e)
            }
        }
    }

    private suspend fun checkAndCreateUserDocument(email: String) {
        try {
            val userDocRef = db.collection("users").document(email)
            val documentSnapshot = userDocRef.get().await()
            if (!documentSnapshot.exists()) {
                val userData = hashMapOf(
                    "email" to email,
                    "favorites" to emptyMap<String, Boolean>()
                )
                userDocRef.set(userData, SetOptions.merge()).await()
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "checkAndCreateUserDocument:failure", e)
        }
    }
}