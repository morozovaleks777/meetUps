package com.morozov.meetups.presentation.home_screen

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dwkidsandroid.presentation.navigation.SharedViewModel
import com.morozov.meetups.R
import com.morozov.meetups.presentation.app_components.AnnotatedStringWithStyles
import com.morozov.meetups.presentation.app_components.AnnotatedStyle
import com.morozov.meetups.presentation.app_components.MeetUpsAppBar
import com.morozov.meetups.presentation.app_components.SystemUI
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
                                    color = generateRandomColor()))
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
                showProfile = true,
                navController = navController,
                onSearchClicked = {},
        )
        }
    ) {it -> 


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Карточки активности
           ActivityCard("Новое фото", R.drawable.ic_launcher_foreground)
            ActivityCard("Обновление статуса", R.drawable.ic_launcher_background)

            // Профиль пользователя
            ProfileCard("John Doe", "28 лет", "Онлайн")

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
fun ProfileCard(name: String, age: String, status: String) {
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
            // Аватар профиля (вставьте свою логику для получения изображения)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
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