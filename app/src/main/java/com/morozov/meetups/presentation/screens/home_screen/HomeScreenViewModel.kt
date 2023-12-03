package com.morozov.meetups.presentation.screens.home_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithme.domain.model.UserStatus
import com.morozov.meetups.domain.usecase.profileScreen.ProfileScreenUseCases
import com.morozov.meetups.domain.usecase.profileScreen.SetUserStatusToFirebase
import com.morozov.meetups.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val  profileScreenUseCases: ProfileScreenUseCases

) : ViewModel() {
    var isUserSignOutState = mutableStateOf(false)
        private set
  fun setUserStatusToFirebase(userStatus: UserStatus) {
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

    fun setUserStatusToFirebaseAndSignOut(userStatus: UserStatus){
        viewModelScope.launch {
            profileScreenUseCases.setUserStatusToFirebase(userStatus).collect{ response ->
                when(response){
                    is Response.Loading -> {}
                    is Response.Success -> {
                        if(response.data){
                           setUserStatusToFirebase(UserStatus.OFFLINE)
                        }else{
                            //Auth.currentuser null.
                        }

                    }
                    is Response.Error -> {}
                }
            }
        }
    }


}
