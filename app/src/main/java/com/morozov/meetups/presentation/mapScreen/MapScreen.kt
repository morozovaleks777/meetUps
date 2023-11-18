package com.morozov.meetups.presentation.mapScreen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(
    navController: NavHostController
) {
    val bucharest = LatLng(44.43, 26.09)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bucharest, 10f)
    }

    val points = listOf(
        LatLng(44.43, 26.09),
        LatLng(44.03, 26.19),
        LatLng(44.83, 26.49)
    )

    GoogleMap(
        cameraPositionState = cameraPositionState,
    ) {
        Polygon(points = points)
    }
}