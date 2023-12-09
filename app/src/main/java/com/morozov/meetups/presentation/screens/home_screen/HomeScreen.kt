package com.morozov.meetups.presentation.screens.home_screen

import android.util.Log
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.chatwithme.domain.model.UserStatus
import com.example.dwkidsandroid.presentation.navigation.SharedViewModel
import com.morozov.meetups.R
import com.morozov.meetups.domain.model.model.User
import com.morozov.meetups.presentation.app_components.AnnotatedStringWithStyles
import com.morozov.meetups.presentation.app_components.AnnotatedStyle
import com.morozov.meetups.presentation.app_components.MeetUpsAppBar
import com.morozov.meetups.presentation.app_components.SystemUI
import com.morozov.meetups.presentation.navigation.AppScreens
import com.morozov.meetups.presentation.screens.profile.ProfileViewModel
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeScreenViewModel,
    sharedViewModel: SharedViewModel,
    transition: Transition<EnterExitState>,
) {
SystemUI()
    Scaffold(
        bottomBar = {
            BottomAppBar() {
Row() {
    Icon(  Icons.Filled.Person,
        modifier = Modifier
            .clickable { navController.navigate(AppScreens.ProfileScreen.name) }
            .scale(1.5f),
        contentDescription = "profile Icon")
    ClickableText(text = AnnotatedString("map"), onClick ={navController.navigate(AppScreens.MapScreen.name)} )
    Spacer(modifier = Modifier.width(10.dp))
    ClickableText(text = AnnotatedString("chat"), onClick ={navController.navigate(AppScreens.ChatScreen.name)} )
}
            }
        },
        topBar = {
            val st = remember {
                mutableStateOf(AnnotatedStyle(0, SpanStyle(fontWeight = FontWeight.Bold, fontSize = 45.sp, color = Color.Magenta)))
            }
            val st2 = remember {
                mutableStateOf(AnnotatedStyle(2, SpanStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.Green)))
            }
            MeetUpsAppBar(
                title = {
                    Column(modifier = Modifier.height(100.dp)) {


                        AnnotatedStringWithStyles(
                            stringResource(id = R.string.app_name),
                            st.value,
                            st2.value,
                        ) {
                            st2.value = AnnotatedStyle(2,
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    color = generateRandomColor()
                                ))
                            st.value = AnnotatedStyle(
                                0,
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 45.sp,
                                    color = generateRandomColor()
                                )
                            )
                        }
                    }
                        },
                setStatus = {homeViewModel.setUserStatusToFirebase(UserStatus.OFFLINE)},
                showProfile = true,
                navController = navController,
                onSearchClicked = {},
        )
        }
    ) {it ->
        val vm: ProfileViewModel = hiltViewModel()
val url = remember {
   vm.userDataStateFromFirebase.value
}

        var userDataFromFirebase by remember { mutableStateOf(User()) }
        userDataFromFirebase = vm.userDataStateFromFirebase.value
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            LaunchedEffect(url ){
          vm.loadProfileFromFirebase()
            }
            // Карточки активности
           ActivityCard("Новое фото", R.drawable.ic_launcher_foreground)
            ActivityCard("Обновление статуса", R.drawable.ic_launcher_background)

            // Профиль пользователя
            ProfileCard(userDataFromFirebase.userName, "28 лет", userDataFromFirebase.status,userDataFromFirebase.userProfilePictureUrl)

        }
    }
}
@Composable
fun ActivityCard(activityText: String, iconResource: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Иконка активности
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )
            // Текст активности
            Text(text = activityText)
        }
    }
}

@Composable
fun ProfileCard(name: String, age: String, status: String,url:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Log.d("imageUrl", "ProfileCard: $url")
            // Аватар профиля (вставьте свою логику для получения изображения)
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .error(R.drawable.ic_eye_closed)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            // Информация о пользователе
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = name, fontWeight = FontWeight.Bold)
                Text(text = age)
                Text(text = status, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}
fun generateRandomColor(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)

    return        Color(red, green, blue)
}