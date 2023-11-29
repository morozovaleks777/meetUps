package com.morozov.meetups.presentation.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.morozov.meetups.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(R.drawable.ic_chatwithme),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        },
        modifier = modifier.statusBarsPadding()
    )
}