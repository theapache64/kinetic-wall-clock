package com.theapache64.klokk.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material.icons.outlined.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.theapache64.klokk.IS_DEBUG
import com.theapache64.klokk.movement.core.Movement
import com.theapache64.klokk.theme.KlokkTheme

@Composable
fun BottomToolBar(
    activeMovement: Movement, // to show debug info
    isAnimPlaying: Boolean,
    textInput: String,
    onShowTimeClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onTextInputChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    theme: MutableState<Boolean>,
) {
    Row(
        modifier = modifier
            .padding(
                top = 18.dp,
                start = 50.dp,
                end = 20.dp,
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Play Button
        if (isAnimPlaying.not()) {
            IconTextButton(
                text = "START AUTOPLAY",
                imageVector = Icons.Outlined.PlayArrow,
                onClicked = onPlayClicked
            )
        }

        // Stop Button
        if (isAnimPlaying) {
            IconTextButton(
                text = "STOP AUTOPLAY",
                imageVector = Icons.Outlined.Stop,
                onClicked = onStopClicked
            )
        }

        // Time Button
        if (isAnimPlaying) {
            IconTextButton(
                text = "SHOW TIME",
                imageVector = Icons.Outlined.Update,
                onClicked = onShowTimeClicked
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // TextField
            /*
            // TODO:
            OutlinedTextField(
                value = textInput,
                onValueChange = onTextInputChanged,
                placeholder = {
                    Text("Try some text here")
                }
            )*/
            // Debug Info
            if (IS_DEBUG) {
                Text(
                    text = "DEBUG: $activeMovement",
                    textAlign = TextAlign.End
                )
            }
            IconButton(
                onClick = {
                    theme.value = !theme.value
                }
            ) {
                if (theme.value) {
                    Icon(imageVector = Icons.Outlined.LightMode, contentDescription = "light mode icon")
                } else {
                    Icon(imageVector = Icons.Outlined.DarkMode,contentDescription = "dark mode icon")
                }
            }
        }
    }
}

@Composable
private fun IconTextButton(
    text: String,
    imageVector: ImageVector,
    onClicked: () -> Unit,
) {

    OutlinedButton(
        onClick = onClicked
    ) {

        Icon(
            imageVector = imageVector,
            contentDescription = "ToolBar Icon",
            modifier = Modifier.padding(end = 10.dp)
        )

        Text(
            text = text
        )
    }
}