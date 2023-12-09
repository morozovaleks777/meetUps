package com.morozov.meetups.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.utils.Response
import kotlinx.coroutines.flow.Flow

interface ILocationService {

    fun requestLocationUpdates(): Flow<LatLng?>

    fun requestCurrentLocation(): Flow<List<LatLng?>>

    fun sendMyLocation(location:LatLng):Flow<Response<Boolean>>
    fun getUserList():  Flow<List<User>>
}