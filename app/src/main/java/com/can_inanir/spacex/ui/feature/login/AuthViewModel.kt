package com.can_inanir.spacex.ui.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
                val task = auth.signInWithEmailAndPassword(email, password).await()
                if (task.user != null) {
                    _userState.value = auth.currentUser
                    checkAndCreateUserDocument(auth.currentUser!!.email!!)
                    _loginErrorState.value = null // Clear error message on success
                } else {
                    _loginErrorState.value = "Your password or e-mail address is wrong."
                }
            } catch (e: Exception) {
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
            val task = auth.createUserWithEmailAndPassword(email, password).await()
            if (task.user != null) {
                _userState.value = auth.currentUser
                checkAndCreateUserDocument(auth.currentUser!!.email!!)
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