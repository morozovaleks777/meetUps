package com.morozov.meetups.domain.model.model

import com.google.android.gms.maps.model.LatLng

data class User(
    var profileUUID: String = "",
    var userEmail: String = "",
    var oneSignalUserId: String = "",
    var userName: String = "",
    var userProfilePictureUrl: String = "",
    var userSurName: String = "",
    var userBio: String = "",
    var userPhoneNumber: String = "",
    var status: String = "",
    var myLocation: LatLngData = LatLngData(LatLng(0.0, 0.0).latitude,LatLng(0.0, 0.0).longitude)
)

data class LatLngData(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)