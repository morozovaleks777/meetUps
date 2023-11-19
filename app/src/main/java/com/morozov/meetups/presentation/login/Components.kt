package com.morozov.meetups.presentation.login



import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.morozov.meetups.ui.theme.ColorsExtra
import com.morozov.meetups.ui.theme.Coral

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Text(text = "Friend Zone",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 45.sp, fontStyle = FontStyle.Italic),
        color = Color.White,
        fontFamily = FontFamily.Serif,
    fontWeight = FontWeight.ExtraBold)

}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)


}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType ,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {

    OutlinedTextField(value = valueState.value,
        onValueChange = { valueState.value  = it},
                shape = RoundedCornerShape(size = 6.dp),
        label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ColorsExtra.SolidLight100,
            unfocusedTextColor = ColorsExtra.SolidLight100,
            errorTextColor = ColorsExtra.SolidLight100,

            focusedContainerColor = Color.White,
            unfocusedContainerColor = ColorsExtra.SolidPink100.copy(0.1f),
            errorContainerColor = ColorsExtra.SolidLight10,

            focusedBorderColor = ColorsExtra.SolidLight80,
            unfocusedBorderColor = ColorsExtra.SolidLight100,
            errorBorderColor = ColorsExtra.SolidRed100,

            focusedLabelColor = ColorsExtra.TransparentLight50,
            unfocusedLabelColor = ColorsExtra.SolidLight50,
            errorLabelColor = ColorsExtra.SolidRed100,

            errorSupportingTextColor = ColorsExtra.SolidRed100,
            focusedSupportingTextColor = ColorsExtra.SolidLight80,
            unfocusedSupportingTextColor = ColorsExtra.SolidLight80,
        )

    )


}



@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)},
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ColorsExtra.SolidLight100,
            unfocusedTextColor = ColorsExtra.SolidLight100,
            errorTextColor = ColorsExtra.SolidLight100,

            focusedContainerColor = Color.White,
            unfocusedContainerColor = ColorsExtra.SolidPink100.copy(0.1f),
            errorContainerColor = ColorsExtra.SolidLight10,

            focusedBorderColor = ColorsExtra.SolidLight80,
            unfocusedBorderColor = ColorsExtra.SolidLight100,
            errorBorderColor = ColorsExtra.SolidRed100,

            focusedLabelColor = ColorsExtra.TransparentLight50,
            unfocusedLabelColor = ColorsExtra.SolidLight50,
            errorLabelColor = ColorsExtra.SolidRed100,

            errorSupportingTextColor = ColorsExtra.SolidRed100,
            focusedSupportingTextColor = ColorsExtra.SolidLight80,
            unfocusedSupportingTextColor = ColorsExtra.SolidLight80,
        )
    )

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible}) {
        Icons.Default.Close

    }

}
@Composable
fun BackgroundImage(
) {
    val mainBgColor = ColorsExtra.SolidDark100
    val gradient = remember {
        Brush.verticalGradient(
            colors = listOf(
                mainBgColor,
                mainBgColor.copy(alpha = 0.2f),
                mainBgColor.copy(alpha = 0.1f),
                mainBgColor.copy(alpha = 0.3f),
                mainBgColor.copy(alpha = 0.8f),
                mainBgColor,
            ),
        )
    }
    Box(
        modifier = Modifier
            .height(300.dp) // It's based on the size of Hero card + various padding so it'd bleed behind
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                spotColor = mainBgColor,
                ambientColor = mainBgColor
            )

    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
               // .data(Drawable.createFromPath("res/drawable/friends_zone.png"))
                .data(Drawable.createFromPath("C:\\Users\\oleksandr.o.morozov\\AndroidStudioProjects\\meetups\\app\\src\\main\\res\\drawable\\friends_zone.png"))
                .crossfade(true)
                .scale(Scale.FIT)
                .build(),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .blur(radiusX = 15.dp, radiusY = 15.dp) // This will be ignored prior to Android 12
                .shadow(
                    elevation = 1.dp,
                    spotColor = mainBgColor,
                    ambientColor = mainBgColor
                )
               // .matchParentSize()
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.SrcAtop)
                    }
                }
                .graphicsLayer(scaleX = 1.5f, scaleY = 1.2f),
            contentScale = ContentScale.Crop,
        )
    }
}