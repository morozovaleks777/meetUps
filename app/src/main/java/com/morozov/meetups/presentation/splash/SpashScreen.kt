package com.morozov.meetups.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.morozov.meetups.presentation.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController){
    Box(modifier = Modifier.background(Color.Green)) {


        Text(text = "Splash")
    }
LaunchedEffect(Unit){
    delay(3000)
    navController.navigate(AppScreens.LoginScreen.name)
}

}