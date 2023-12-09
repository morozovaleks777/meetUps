package com.morozov.meetups.presentation.screens.mapScreen

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.extension.hasLocationPermission
import kotlinx.coroutines.Deferred
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavHostController
) {
    val locationViewModel: MapVM = hiltViewModel()
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val context = LocalContext.current
    val viewState by locationViewModel.viewState.collectAsStateWithLifecycle()
    val users = locationViewModel.users.collectAsState()
    Timber.tag("userDataStateFromFirebase").d("MapScreen: %s", users)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        LaunchedEffect(!context.hasLocationPermission()) {
            permissionState.launchMultiplePermissionRequest()
        }

        when {
            permissionState.allPermissionsGranted -> {
                LaunchedEffect(Unit) {
                    locationViewModel.handle(PermissionEvent.Granted)
                    locationViewModel.sendLocation(PermissionEvent.Granted, LatLng(1.0, 0.1))
                }
            }

            permissionState.shouldShowRationale -> {
                RationaleAlert(onDismiss = { }) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }

            !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                LaunchedEffect(Unit) {
                    locationViewModel.handle(PermissionEvent.Revoked)
                }
            }
        }

        with(viewState) {
            when (this) {
                ViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ViewState.RevokedPermissions -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("We need permissions to use this app")
                        Button(
                            onClick = {
                                startActivity(
                                    context,
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                    null
                                )
                            },
                            enabled = !context.hasLocationPermission()
                        ) {
                            if (context.hasLocationPermission()) CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = Color.White
                            )
                            else Text("Settings")
                        }
                    }
                }

                is ViewState.Success -> {
                    val currentLoc =
                        LatLng(
                            this.location?.latitude ?: 0.0,
                            this.location?.longitude ?: 0.0
                        )
                    val cameraState = rememberCameraPositionState()

                    LaunchedEffect(key1 = currentLoc) {
                        cameraState.centerOnLocation(currentLoc)
                        locationViewModel.sendLocation(PermissionEvent.Granted, currentLoc)
                        locationViewModel.getUserList()


                    }
                    LaunchedEffect(UShort) {
                        locationViewModel.sendLocation(PermissionEvent.Granted, currentLoc)
                    }

                    MainScreen(
                        currentPosition = LatLng(
                            currentLoc.latitude,
                            currentLoc.longitude
                        ),
                        usersList = users.value,
                        cameraState = cameraState
                    ) { url -> locationViewModel.getMarkerImage(url) }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    currentPosition: LatLng,
    cameraState: CameraPositionState, usersList: List<User>,
    getMarkerImage: (String) -> Deferred<BitmapDescriptor?>
) {

    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    GoogleMap(
        uiSettings = uiSettings,
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.HYBRID,
            isTrafficEnabled = true
        )
    ) {
        for (user in usersList) {
            val markerIconState = remember(user.userProfilePictureUrl) {
                mutableStateOf<BitmapDescriptor?>(null)
            }

            LaunchedEffect(user.userProfilePictureUrl) {
                markerIconState.value = getMarkerImage(user.userProfilePictureUrl).await()
            }

            Timber.tag("userDataStateFromFirebase").d("MainScreen: %s", user.userName)

            Marker(
                icon = markerIconState.value,
                tag = user.profileUUID,
                onClick = {
                    Timber.tag("userDataStateFromFirebase").d("MainScreen: %s", user.myLocation)
                    true
                },
                visible = true,
                state = MarkerState(
                    position = LatLng(
                        user.myLocation.latitude,
                        user.myLocation.longitude
                    )
                ),
                title = "MyPosition",
                snippet = "This is a description of this Marker",
                draggable = true,
            )
        }
    }


}


@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "We need location permissions to use this app",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}


private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        15f
    ),
    durationMs = 1500
)

