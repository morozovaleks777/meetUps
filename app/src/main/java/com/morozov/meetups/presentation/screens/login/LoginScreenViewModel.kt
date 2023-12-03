package com.morozov.meetups.presentation.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithme.domain.model.UserStatus
import com.example.chatwithme.domain.usecase.authScreen.AuthUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.morozov.meetups.domain.MUser
import com.morozov.meetups.domain.usecase.profileScreen.ProfileScreenUseCases
import com.morozov.meetups.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val  authUseCases: AuthUseCases,
    private val  profileScreenUseCases: ProfileScreenUseCases
) : ViewModel() {

    var isUserAuthenticatedState = mutableStateOf(false)
        private set
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    val errorMessage = MutableStateFlow("")


    init {
        isUserAuthenticated()
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            if (isUserAuthenticatedState.value) {
                home()
            } else {
                if (isEmailValid(email)) {
                    authUseCases.signIn
//                    try {
//                        auth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    Timber.tag("FB")
//                                        .d(
//                                            "signInWithEmailAndPassword: Yayayay! %s",
//                                            task.result.toString()
//                                        )
//                                    setUserStatusToFirebase(UserStatus.ONLINE)
//                                    home()
//                                } else {
//                                    Timber.tag("FB")
//                                        .d(
//                                            "signInWithEmailAndPassword: %s",
//                                            task.exception?.message
//                                        )
//                                    errorMessage.value =
//                                        "Authentication failed: ${task.exception?.message}"
//                                }
//                            }
//
//                    } catch (ex: Exception) {
//                        errorMessage.value = "Authentication failed"
//                        Timber.tag("FB").d("signInWithEmailAndPassword: %s", ex.message)
//                    }

                } else {
                    errorMessage.value = "Invalid email format"
                }
            }
        }
    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //me
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Timber.tag("FB")
                            .d("createUserWithEmailAndPassword: %s", task.result.toString())

                    }
                    _loading.value = false

                }
        }


    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is great",
            profession = "Android Developer",
            id = null
        ).toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)


    }

    private fun isUserAuthenticated() {
        viewModelScope.launch {
            authUseCases.isUserAuthenticated().collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {
                        isUserAuthenticatedState.value = response.data
                        if (response.data) {
                            setUserStatusToFirebase(UserStatus.ONLINE)
                        }
                    }
                    is Response.Error -> {}
                }
            }
        }
    }

    private fun setUserStatusToFirebase(userStatus: UserStatus) {
        viewModelScope.launch {
            profileScreenUseCases.setUserStatusToFirebase(userStatus).collect { response ->
                when (response) {
                    is Response.Loading -> {}
                    is Response.Success -> {}
                    is Response.Error -> {}
                }
            }
        }
    }

}