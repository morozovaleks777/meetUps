package com.morozov.meetups.presentation.screens.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.morozov.meetups.R
import com.morozov.meetups.presentation.app_components.SystemUI
import com.morozov.meetups.presentation.navigation.AppScreens
import com.morozov.meetups.ui.theme.ColorsExtra
import com.morozov.meetups.ui.theme.Coral


@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    val colorStart = Coral
    val colorMid = Coral.copy(0.7f)
    val colorEnd = ColorsExtra.SolidPink100
    val gradientColor = remember {
        Brush.linearGradient(
            end = Offset.Infinite,
            colors = listOf(colorStart, colorMid, colorEnd)
        )
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val errorMessage = viewModel.errorMessage.collectAsState().value

    SystemUI()

    val isUserAuthenticated = viewModel.isUserAuthenticatedState.value
    LaunchedEffect(Unit) {
        if (isUserAuthenticated) {
            navController.navigate(AppScreens.HomeScreen.name)
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(gradientColor)
                .padding(top = statusBarPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppLogo()
            if (showLoginForm.value) UserForm(
                errorText = errorMessage,
                loading = false,
                isCreateAccount = false
            ) { email, password ->
                viewModel.signInWithEmailAndPassword(email, password) {
                    navController.navigate(AppScreens.HomeScreen.name)

                }
            }
            else {
                UserForm(
                    loading = false,
                    isCreateAccount = true,
                    errorText = errorMessage
                ) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(AppScreens.HomeScreen.name)
                    }
                }
            }


            Spacer(modifier = Modifier.height(15.dp))


            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text = if (showLoginForm.value) "Sign up" else "Login"
                Text(text = if (showLoginForm.value) "New User?" else "Already have account?",
                    color = Color.White)
                Text(text,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value

                        }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    textDecoration =  TextDecoration.Underline,
                    color = Color.White)

            }
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(R.drawable.friends_zone),
                contentDescription = "logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize(),
                colorFilter = ColorFilter.lighting(
                    Color.White,
                    Coral.copy(alpha = 0.7f, blue = 0.4f)
                )

            )
        }
    }
}
