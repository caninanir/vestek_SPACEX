package com.can_inanir.spacex.authandapi


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@SuppressLint("StaticFieldLeak")
val db: FirebaseFirestore = FirebaseFirestore.getInstance()

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            user?.let {
                checkAndCreateUserDocument(it.email!!)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("AuthViewModel", "signInWithEmail:success")
                            _userState.value = auth.currentUser
                            auth.currentUser?.let { checkAndCreateUserDocument(it.email!!) }
                        } else {
                            Log.w("AuthViewModel", "signInWithEmail:failure", task.exception)
                        }
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
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("AuthViewModel", "createUserWithEmail:success")
                            _userState.value = auth.currentUser
                            auth.currentUser?.let { checkAndCreateUserDocument(it.email!!) }
                        } else {
                            Log.w("AuthViewModel", "createUserWithEmail:failure", task.exception)
                        }
                    }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "createAccount:failure", e)
            }
        }
    }

    fun handleGoogleAccessToken(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "signInWithCredential:success")
                    _userState.value = auth.currentUser
                } else {
                    Log.w("AuthViewModel", "signInWithCredential:failure", task.exception)
                }
            }
    }
}



private fun checkAndCreateUserDocument(email: String) {
    val userDocRef = db.collection("users").document(email)
    userDocRef.get().addOnSuccessListener { documentSnapshot ->
        if (!documentSnapshot.exists()) {
            val userData = hashMapOf(
                "email" to email,
                "favorites" to emptyMap<String, Boolean>()
            )
            userDocRef.set(userData, SetOptions.merge())
        }
    }
}
