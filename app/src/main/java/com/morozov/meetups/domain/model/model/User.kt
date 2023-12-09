package com.morozov.meetups.domain.model.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("profileUUID")
    var profileUUID: String = "",
    @SerializedName("userEmail")
    var userEmail: String = "",
    @SerializedName("oneSignalUserId")
    var oneSignalUserId: String = "",
    @SerializedName("userName")
    var userName: String = "",
    @SerializedName("userProfilePictureUrl")
    var userProfilePictureUrl: String = "",
    @SerializedName("userSurName")
    var userSurName: String = "",
    @SerializedName("userBio")
    var userBio: String = "",
    @SerializedName("userPhoneNumber")
    var userPhoneNumber: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("myLocation")
    var myLocation: LatLngData = LatLngData(LatLng(0.0, 0.0).latitude,LatLng(0.0, 0.0).longitude)
)

data class LatLngData(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)