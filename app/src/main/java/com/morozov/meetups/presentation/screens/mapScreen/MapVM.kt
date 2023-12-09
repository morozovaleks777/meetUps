package com.morozov.meetups.presentation.screens.mapScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.domain.usecase.location.GetLocationUseCase
import com.morozov.meetups.domain.usecase.mapScreen.MapUseCase
import com.morozov.meetups.utils.GetBitmapFromURL
import com.morozov.meetups.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class MapVM @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val mapUseCase: MapUseCase
) : ViewModel() {

    val users: MutableStateFlow<List<User>> = MutableStateFlow(
        mutableListOf()
    )

    //val bitMapState = mutableStateOf<BitmapDescriptor?>(null)
    val bitMapState = MutableStateFlow<BitmapDescriptor?>(null)

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    fun handle(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Granted -> {
                viewModelScope.launch {
                    getLocationUseCase.invoke().collect {
                        _viewState.value = ViewState.Success(it)
                    }
                }
            }

            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    fun sendLocation(event: PermissionEvent, location: LatLng?) {
        when (event) {
            PermissionEvent.Granted -> {
                viewModelScope.launch {
                    mapUseCase.sendMyLocat(location).collect { response ->
                        when (response) {
                            is Response.Loading -> {}
                            is Response.Success -> {

                            }

                            else -> {}
                        }
                    }
                }
            }

            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    fun getUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            mapUseCase.getListUser().collect {
                users.value = it
            }
        }
    }

    //    fun getMarkerImage(url:String){
//        viewModelScope.async(Dispatchers.Default) {
//
//            GetBitmapFromURL.getBitmapFromURL(url)?.let { bm ->
//                GetBitmapFromURL.getResizedBitmap(bm, 150, 150)?.let { bitmap ->
//                    GetBitmapFromURL.getRoundedCornerBitmap(bitmap)?.let {
//                        bitMapState.value =
//                            BitmapDescriptorFactory.fromBitmap(it)
//                    }
//                }
//            }
//        }
//     bitMapState.value
//    }
//
//}
    fun getMarkerImage(url: String): Deferred<BitmapDescriptor?> =
        viewModelScope.async(Dispatchers.Default) {
            var result: BitmapDescriptor? = null

            GetBitmapFromURL.getBitmapFromURL(url)?.let { bm ->
                GetBitmapFromURL.getResizedBitmap(bm, 150, 150)?.let { bitmap ->
                    GetBitmapFromURL.getRoundedCornerBitmap(bitmap)?.let {
                        result = BitmapDescriptorFactory.fromBitmap(it)
                    }
                }
            }

            result
        }
}
sealed interface ViewState {
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}

sealed interface PermissionEvent {
    object Granted : PermissionEvent
    object Revoked : PermissionEvent
}

