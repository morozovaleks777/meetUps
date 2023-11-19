package com.morozov.meetups.presentation.login



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.morozov.meetups.R
import com.morozov.meetups.ui.theme.ColorsExtra
import com.morozov.meetups.ui.theme.Coral

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Text(text = "FriendsZone",
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
    errorText:String = "",
    supportText: String = "",
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType ,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    var showPassword by remember { mutableStateOf(false) }
    val visualTransformation = if (showPassword || keyboardType != KeyboardType.Password) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

        OutlinedTextField(
            value =valueState.value,
        onValueChange = {
            valueState.value = it
        },
        label = { Text(text = labelId)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        supportingText = {
            val extraText = errorText.ifEmpty { supportText }
            if (extraText.isNotEmpty()) {
                Text(text = extraText)
            }
        },
        trailingIcon = {
            if (keyboardType == KeyboardType.Password) {
                IconButton(onClick = {showPassword = !showPassword }) {
                    Icon(
                        painter = if (showPassword) {
                            painterResource(R.drawable.ic_eye_closed)
                        } else {
                            painterResource(R.drawable.ic_eye_open)
                        },
                        contentDescription = stringResource(R.string.password_visibility),
                        modifier = Modifier.size(24.dp),
                        tint = ColorsExtra.SolidDark100
                    )
                }
            }
        },
        keyboardActions = onAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ColorsExtra.SolidLight100,
            unfocusedTextColor = ColorsExtra.SolidLight100,
            errorTextColor = ColorsExtra.SolidDark100,

            focusedContainerColor = Color.White,
            unfocusedContainerColor = ColorsExtra.SolidPink100.copy(0.1f),
            errorContainerColor = ColorsExtra.SolidLight10,

            focusedBorderColor = ColorsExtra.SolidLight80,
            unfocusedBorderColor = ColorsExtra.SolidLight100,
            errorBorderColor = ColorsExtra.SolidRed100,

            focusedLabelColor = ColorsExtra.TransparentLight50,
            unfocusedLabelColor = ColorsExtra.SolidLight50,
            errorLabelColor = ColorsExtra.SolidRed100,

            errorSupportingTextColor = ColorsExtra.SolidDark100,
            focusedSupportingTextColor = ColorsExtra.SolidDark100,
            unfocusedSupportingTextColor = ColorsExtra.SolidDark100,
        )
    )


}



@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Password,
    labelId: String,
    enabled: Boolean,
    errorText:String = "Error",
    supportText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {
    InputField(
        modifier = modifier,
        valueState = passwordState,
        labelId =  labelId,
        enabled = enabled,
        keyboardType = keyboardType,
        imeAction = imeAction,
        errorText = errorText,
        supportText = supportText,
        onAction = onAction)
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    errorText: String = "error",
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()

    }
    val modifier = Modifier
        .wrapContentSize()
        .background(Color.Transparent)
        .verticalScroll(rememberScrollState())


    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCreateAccount) Text(
            text = stringResource(R.string.create_acct),
            modifier = Modifier.padding(4.dp)
        ) else Text("")
        EmailInput(
            emailState = email, enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
        )
        PasswordInput(
            errorText = errorText,
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading, //Todo change this
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            })

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Coral.copy(alpha = 0.8f)),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = RoundedCornerShape(size = 4.dp)
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))

    }
}