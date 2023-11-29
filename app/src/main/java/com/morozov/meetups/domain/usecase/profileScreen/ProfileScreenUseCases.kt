package com.morozov.meetups.domain.usecase.profileScreen

import com.example.chatwithme.domain.usecase.profileScreen.UploadPictureToFirebase

data class ProfileScreenUseCases(
    val createOrUpdateProfileToFirebase: CreateOrUpdateProfileToFirebase,
    val loadProfileFromFirebase: LoadProfileFromFirebase,
    val setUserStatusToFirebase: SetUserStatusToFirebase,
    val signOut: SignOut,
    val uploadPictureToFirebase: UploadPictureToFirebase
)