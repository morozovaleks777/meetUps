package com.morozov.meetups.presentation.navigation

import androidx.navigation.NavController

enum class AppScreens {
    SplashScreen,
    LoginScreen,
    ProfileScreen,
    DetailScreen,
    DetailScreenTablet,
    VideoPlayerScreen,
    HomeScreen,
    AccountScreen,
    BillingDetailsScreen,
    HelpScreen,
    LegalScreen,
    MapScreen,
    ContentLockScreen,
    ;

    companion object {
        fun String.asAppScreen(): AppScreens {
            return values().find { contains(it.name, ignoreCase = true) }
                ?: throw IllegalArgumentException("This string is NOT a route")
        }

        fun resolve(navController: NavController, screen: AppScreens, isWideScreen: Boolean) {
            navController.navigate(
                if (isWideScreen) {
                    when (screen) {
                        DetailScreen -> DetailScreenTablet.name

                        else -> screen.name
                    }
                } else {
                    screen.name
                }
            )
        }
    }
}