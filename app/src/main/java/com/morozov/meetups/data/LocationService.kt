package com.morozov.meetups.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.morozov.meetups.core.Constants
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.domain.repository.ILocationService
import com.morozov.meetups.extension.hasLocationPermission
import com.morozov.meetups.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationService @Inject constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient,
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : ILocationService {
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun requestLocationUpdates(): Flow<LatLng?> = callbackFlow<LatLng?> {

        if (!context.hasLocationPermission()) {
            trySend(null)
            return@callbackFlow
        }

        val request = LocationRequest.Builder(100000L)
            .setIntervalMillis(100000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }.flowOn(Dispatchers.IO)

    override fun requestCurrentLocation(): Flow<List<LatLng?>> {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun sendMyLocation(location: LatLng): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                val userUUID = auth.currentUser?.uid?.trim().toString()
                val userEmail = auth.currentUser?.email.toString()
                //  val oneSignalUserId = OneSignal.getDeviceState()?.userId.toString()
                val databaseReference =
                    database.getReference("Profiles").child(userUUID).child("profile")
                val childUpdates = mutableMapOf<String, Any>()

                childUpdates["/profileUUID/"] = userUUID
                childUpdates["/userEmail/"] = userEmail
                //   childUpdates["/oneSignalUserId/"] = oneSignalUserId
                childUpdates["/myLocation/"] = location
                databaseReference.updateChildren(childUpdates).await()
                emit(Response.Success(true))
            } catch (e: Exception) {
                emit(Response.Error(e.message ?: Constants.ERROR_MESSAGE))
            }
        }


    override fun getUserList(): Flow<List<User>> = callbackFlow {
        val usersRef = database.getReference("Profiles")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersList = dataSnapshot.children.map {
                    it.child("profile").getValue(User::class.java)!!
                }
                Log.d("getUserList", "Received data: ${usersList}")
                this@callbackFlow.trySend(usersList.toList()).isSuccess
                close()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("getUserList", "Error fetching data: $databaseError")
                close(databaseError.toException())
            }
        }

        usersRef.addListenerForSingleValueEvent(valueEventListener)

        awaitClose {
            usersRef.removeEventListener(valueEventListener)
        }
    }.flowOn(Dispatchers.IO)




}


