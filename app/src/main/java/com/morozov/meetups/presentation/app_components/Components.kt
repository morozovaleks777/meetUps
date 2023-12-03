package com.morozov.meetups.presentation.app_components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.morozov.meetups.presentation.navigation.AppScreens
import com.morozov.meetups.ui.theme.ColorsExtra
import com.morozov.meetups.utils.StringUtils


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetUpsAppBar(
    title: @Composable () -> Unit,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    setStatus:() -> Unit = {},
    onSearchClicked:() -> Unit = {},
    onBackArrowClicked:() -> Unit = {}
) {

    TopAppBar(
        title = {
            Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center ){
                if (showProfile) {
                    Icon(imageVector = Icons.Default.Search,
                        contentDescription = "search Icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.9f)
                            .clickable {
                                onSearchClicked.invoke()
                            }
                    )
                }
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = "arrow back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() })
                }

                Spacer(modifier = Modifier.width(40.dp) )
                title.invoke()

            }
       },
        actions = {
            IconButton(
                onClick = {
                FirebaseAuth.getInstance()
                    .signOut().run {
                       setStatus()
                        navController.navigate(AppScreens.LoginScreen.name)
                    }
               }
            ) {
                    if (showProfile) Row() {
                        Icon(imageVector = Icons.Filled.Logout ,
                            contentDescription = "Logout" ,
                            // tint = Color.Green.copy(alpha = 0.4f)
                        )
                    } else Box {}
              }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent ),
    )
}

@Composable
fun AnnotatedString(inputString: String,){
    val words = inputString.split(" ")

    val annotatedText = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            val style = when (index % 3) {
                0 -> SpanStyle(color = Color.Red)
                1 -> SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 30.sp)
                else -> SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
            }
            withStyle(style) {
                append(word)
                if (index < words.size - 1) {
                    append(" ")
                }
            }
        }
    }
    ClickableText( text = annotatedText, onClick = { offset ->
        // Handle click here
      //  println("You clicked on offset $offset")
    })
}

data class AnnotatedStyle(
    val wordIndex: Int,
    val style: SpanStyle
)

@Composable
fun AnnotatedStringWithStyles(
    inputString: String,
    vararg annotatedStyles: AnnotatedStyle,
    someStyle: () -> Unit = {}
) {
    val words = inputString.split(" ")
    Log.d("st", "AnnotatedStringWithStyles: $words ")
    val annotatedText = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            val matchingStyle = annotatedStyles.find { it.wordIndex == index }
            val style = matchingStyle?.style ?: SpanStyle()
            withStyle(style) {
                append(word)
                if (index < words.size - 1) {
                    append(" ")
                }
            }
        }
    }

    //  Text(text = annotatedText, textAlign = TextAlign.Center )
    ClickableText(modifier = Modifier.wrapContentHeight(), text = annotatedText,
        style = TextStyle(textAlign = TextAlign.Center, lineHeight = 20.sp,// color = Color.Green
        ),
        onClick = { offset ->
            someStyle.invoke()

        })
}

@Composable
fun DefaultClickableText(
    text: String,
    clickableTexts: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    textStyle: SpanStyle = SpanStyle(
        color = ColorsExtra.SolidGreen100,
        fontSize = 14.sp,
        fontWeight = FontWeight(600),
       // fontFamily = FontFamily(Font(R.font.)),
        textDecoration = TextDecoration.Underline,
    ),
    clickableTextStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = ColorsExtra.LightBlue,
        textAlign = TextAlign.Center
    ),
    onTextClick: (String) -> Unit,
) {
    val (annotatedString, tag) = StringUtils.multiLinkAnnotatedString(
        text, clickableTexts,
        style = textStyle
    )
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = clickableTextStyle,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = tag, start = offset, end = offset
            ).firstOrNull()?.let { annotation ->
                onTextClick(annotation.item)
            }
        }
    )
}

@Composable
fun LogOutCustomText(
    entry: String = "Log Out",
    fontSize: TextUnit = 10.sp,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
        Text(
            text = "Log Out",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onError
        )
    }

//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(percent = 50))
//            .background(
//                MaterialTheme.colorScheme.surface
//            )
//            .height(26.dp)
//            .fillMaxWidth()
//            .clickable {
//                onClick()
//            }) {
//        Text(
//            modifier = Modifier.align(Alignment.Center),
//            text = entry,
//            fontSize = fontSize,
//            color = Color.Red
//        )
//    }
}