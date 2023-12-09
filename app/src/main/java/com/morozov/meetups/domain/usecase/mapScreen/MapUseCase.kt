package com.morozov.meetups.domain.usecase.mapScreen

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.domain.repository.ILocationService
import javax.inject.Inject

class MapUseCase @Inject constructor(
    private val locationService: ILocationService,
   private val user: User
) {
    @RequiresApi(Build.VERSION_CODES.S)
   fun sendMyLocat(location: LatLng?) =
        locationService.sendMyLocation(location ?: LatLng(0.3,0.3))

   fun  getListUser() = locationService.getUserList()
}