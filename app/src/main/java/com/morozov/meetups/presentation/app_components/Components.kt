package com.morozov.meetups.presentation.app_components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

@Composable
fun SystemUI(
    view: View = LocalView.current,
    systemUiController: SystemUiController = rememberSystemUiController(),
    configuration: Configuration = LocalConfiguration.current
) {
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false, isNavigationBarContrastEnforced = true,
            transformColorForLightContent = { Color.Transparent })

        //  keeps hidden and on swipe transparent bars are shown
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            systemUiController.isNavigationBarVisible = false
            systemUiController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val window = view.context.getActivity()!!.window
            window.navigationBarColor = Color.Transparent.toArgb()
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }



    val window = view.context.getActivity()!!.window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}