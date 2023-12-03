package com.morozov.meetups.data

import com.google.firebase.auth.FirebaseAuth
import com.morozov.meetups.core.Constants.ERROR_MESSAGE
import com.morozov.meetups.domain.repository.AuthScreenRepository
import com.morozov.meetups.utils.Response
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthScreenRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthScreenRepository {
    override fun isUserAuthenticatedInFirebase(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            if (auth.currentUser != null) {
                emit(Response.Success(true))
            } else {
                emit(Response.Success(false))
            }
        } catch (e: Exception) {
            emit(Response.Success(false))
        }
    }

    override suspend fun signIn(email: String, password: String): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                auth.signInWithEmailAndPassword(email, password).apply {
                    this.await()
                    if (this.isSuccessful) {
                        this@callbackFlow.trySendBlocking(Response.Success(true))
                    } else {
                        this@callbackFlow.trySendBlocking(Response.Success(false))
                    }
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error(e.message ?: ERROR_MESSAGE))
            }

            awaitClose {
                channel.close()
                cancel()
            }
        }

    override suspend fun signUp(email: String, password: String): Flow<Response<Boolean>> =
        callbackFlow {
            try {
                this@callbackFlow.trySendBlocking(Response.Loading)
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    if (it.user != null) {
                        this@callbackFlow.trySendBlocking(Response.Success(true))
                    }
                }.addOnFailureListener {
                    this@callbackFlow.trySendBlocking(Response.Error(it.message ?: ERROR_MESSAGE))
                }
            } catch (e: Exception) {
                this@callbackFlow.trySendBlocking(Response.Error(e.message ?: ERROR_MESSAGE))
            }
            awaitClose {
                channel.close()
                cancel()
            }
        }
}