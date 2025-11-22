package com.bergenproduction.pilltracker.core.ui.designsystem.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalViewConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LongPressableTab(
    selected: Boolean,
    onClick: () -> Unit,
    onLongPress: (Offset) -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val viewConfiguration = LocalViewConfiguration.current
    var globalOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(interactionSource) {
        var isLongClick = false

        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    interaction
                    onLongPress.invoke(
                        Offset(
                            x = globalOffset.x + interaction.pressPosition.x,
                            y = globalOffset.y + interaction.pressPosition.y
                        )
                    )
                    println(interaction.pressPosition)
                    println(globalOffset)
                }

                is PressInteraction.Release -> {
                    if (!isLongClick) onClick.invoke()
                }
            }
        }
    }

    Tab(
        selected,
        onClick = {},
        interactionSource = interactionSource,
        text = text,
        modifier = modifier.onGloballyPositioned {
            globalOffset = it.positionInWindow().copy(
                y = it.positionInWindow().y + it.size.height
            )
        }
    )
}