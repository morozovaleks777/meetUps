package com.morozov.meetups.presentation.screens.home_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatwithme.domain.model.UserStatus
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.domain.repository.ILocationService
import com.morozov.meetups.domain.usecase.profileScreen.ProfileScreenUseCases
import com.morozov.meetups.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val  profileScreenUseCases: ProfileScreenUseCases,
    private val locationService: ILocationService

) : ViewModel() {
    var isUserSignOutState = mutableStateOf(false)
        private set

    var listNewUsers = MutableStateFlow<List<User>>(mutableListOf())

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
    fun getNewUsersList(){
        viewModelScope.launch(Dispatchers.IO) {
            locationService.getUserList().collect {
                listNewUsers.value = it
                it.filterNot { user ->
                    user.registrationDate.isEmpty()
                }.filter { user -> user.registrationDate.toLong() in getMillisForDayAgo()..System.currentTimeMillis() }
            }
        }
    }

    private fun getMillisForDayAgo(): Long {
        val instant = Instant.now().minus(1, ChronoUnit.DAYS)
        return instant.toEpochMilli()
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
