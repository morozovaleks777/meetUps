package com.morozov.meetups.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.morozov.meetups.domain.MUser
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginScreenViewModel:ViewModel() {
    // val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit )
            = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.tag("FB")
                            .d("signInWithEmailAndPassword: Yayayay! %s", task.result.toString())

                        home()
                    }else {
                        Timber.tag("FB").d("signInWithEmailAndPassword: %s", task.result.toString())
                    }


                }

        }catch (ex: Exception) {
            Timber.tag("FB").d("signInWithEmailAndPassword: %s", ex.message)
        }


    }



    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //me
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    }else {
                        Timber.tag("FB")
                            .d("createUserWithEmailAndPassword: %s", task.result.toString())

                    }
                    _loading.value = false


                }
        }


    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = MUser(userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is great",
            profession = "Android Developer",
            id = null).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)







    }


}