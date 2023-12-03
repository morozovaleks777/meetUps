package com.morozov.meetups.presentation.screens.mapScreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.provider.Settings
import android.util.Log
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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
import com.morozov.meetups.presentation.screens.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
                                startActivity( context,  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),null)
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
                    }

                    MainScreen(
                        currentPosition = LatLng(
                            currentLoc.latitude,
                            currentLoc.longitude
                        ),
                        cameraState = cameraState
                    )
                }
            }
        }
    }
}



@Composable
fun MainScreen(currentPosition: LatLng, cameraState: CameraPositionState) {
    val markers = listOf( LatLng(currentPosition.latitude, currentPosition.longitude),LatLng(currentPosition.latitude+0.0005, currentPosition.longitude))
    val profileViewModel:ProfileViewModel = hiltViewModel()
    var userDataFromFirebase by remember { mutableStateOf(User()) }
        userDataFromFirebase = profileViewModel.userDataStateFromFirebase.value


    val bitMapState = remember {
        mutableStateOf<BitmapDescriptor?>(null)
    }
    LaunchedEffect(userDataFromFirebase){
        profileViewModel.loadProfileFromFirebase()
        getBitmapFromURL(userDataFromFirebase.userProfilePictureUrl)?.let { bm ->
            getResizedBitmap(bm, 150, 150)?.let { bitmap ->
                getRoundedCornerBitmap(bitmap)?.let {
                    bitMapState.value =
                        BitmapDescriptorFactory.fromBitmap(it)
                }
            }
        }
    }

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
     for (marker in markers){


    Marker(
      icon = bitMapState.value,
      tag = "i am here",
        onClick = {
            Log.d("markerclick", "MainScreen: $marker ")
            true
        },
        visible = true,
        state = MarkerState(position = marker),
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



fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
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


suspend fun getBitmapFromURL(imgUrl: String?): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val url = URL(imgUrl)
            val connection: HttpURLConnection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
// CREATE A MATRIX FOR THE MANIPULATION
 val matrix = Matrix()
// RESIZE THE BIT MAP
 matrix.postScale(scaleWidth, scaleHeight)
// "RECREATE" THE NEW BITMAP
 val resizedBitmap = Bitmap.createBitmap( bm, 0, 0, width, height, matrix, false)
    bm.recycle()
    return resizedBitmap }

fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap? {
    val w=bitmap.width
    val h=bitmap.height
    val radius = (h / 2).coerceAtMost(w / 2)
    val output = Bitmap.createBitmap(w + 16, h + 16, Bitmap.Config.ARGB_8888)
    val paint = Paint()
    paint.isAntiAlias = true
    val canvas = Canvas(output)
    canvas.drawARGB(0, 0, 0, 0)
    paint.style = Paint.Style.FILL
    canvas.drawCircle((w / 2 + 8).toFloat(), (h / 2 + 8).toFloat(), radius.toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, 4f, 4f, paint)
    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 10f
    canvas.drawCircle((w / 2 + 8).toFloat(), (h / 2 + 8).toFloat(), radius.toFloat(), paint)
    return output }


