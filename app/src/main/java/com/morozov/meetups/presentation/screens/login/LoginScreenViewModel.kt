package com.morozov.meetups.presentation.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithme.domain.model.UserStatus
import com.morozov.meetups.domain.usecase.authScreen.AuthUseCases
import com.morozov.meetups.domain.usecase.profileScreen.ProfileScreenUseCases
import com.morozov.meetups.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val  authUseCases: AuthUseCases,
    private val  profileScreenUseCases: ProfileScreenUseCases
) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    val errorMessage = MutableStateFlow("")

    var isUserAuthenticatedState = mutableStateOf(false)
        private set

    private var isUserSignInState = mutableStateOf(false)

    private var isUserSignUpState = mutableStateOf(false)

    private var toastMessage = mutableStateOf("")

    init {
        isUserAuthenticated()
    }



    fun signInWithEmailAndPassword(email: String, password: String ,home: () -> Unit) {
        viewModelScope.launch {
            authUseCases.signIn(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                      //  toastMessage.value = ""
                    }
                    is Response.Success -> {
                        setUserStatusToFirebase(UserStatus.ONLINE)
                        isUserSignInState.value = response.data
                        toastMessage.value = "Login Successful"
                        home()
                    }
                    is Response.Error -> {
                        errorMessage.value =
                            "Authentication failed: ${response.message}"
                        toastMessage.value = "Login Failed"
                    }
                }
            }
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        viewModelScope.launch {
            authUseCases.signUp(email, password).collect { response ->
                when (response) {
                    is Response.Loading -> {
                        toastMessage.value = ""
                    }
                    is Response.Success -> {
                        isUserSignUpState.value = response.data
                        toastMessage.value = "Sign Up Successful"
                        home()
                    }
                    is Response.Error -> {
                        try {
                            errorMessage.value =
                                "Authentication failed: ${response.message}"
                            toastMessage.value = "Sign Up Failed"
                        }catch (e: Exception) {
                            Timber.tag("TAG").e(Throwable(e), "signUp: ")
                        }
                    }
                }
            }
        }


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