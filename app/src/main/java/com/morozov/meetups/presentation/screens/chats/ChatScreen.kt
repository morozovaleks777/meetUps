package com.morozov.meetups.presentation.screens.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatwithme.domain.model.UserStatus
import com.morozov.meetups.R
import com.morozov.meetups.presentation.app_components.AnnotatedStringWithStyles
import com.morozov.meetups.presentation.app_components.AnnotatedStyle
import com.morozov.meetups.presentation.app_components.MeetUpsAppBar
import com.morozov.meetups.presentation.navigation.AppScreens
import com.morozov.meetups.presentation.screens.home_screen.generateRandomColor

@Composable
fun ChatScreen(navController: NavController,){

    Scaffold(
        bottomBar = {
            BottomAppBar() {
                Row() {
                    Icon(  Icons.Filled.Person,
                        modifier = Modifier
                            .clickable { navController.navigate(AppScreens.ProfileScreen.name) }
                            .scale(1.5f),
                        contentDescription = "profile Icon")
                    ClickableText(text = AnnotatedString("map"), onClick ={navController.navigate(
                        AppScreens.MapScreen.name)} )
                    Spacer(modifier = Modifier.width(10.dp))
                    ClickableText(text = AnnotatedString("chat"), onClick ={navController.navigate(
                        AppScreens.ChatScreen.name)} )
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
                                )
                            )
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
                setStatus = {},
                showProfile = false,
                navController = navController,
                onSearchClicked = {},
                icon = Icons.Default.ArrowBack,
                onBackArrowClicked = {navController.popBackStack()}
            )
        }
    ) {it

    }

}