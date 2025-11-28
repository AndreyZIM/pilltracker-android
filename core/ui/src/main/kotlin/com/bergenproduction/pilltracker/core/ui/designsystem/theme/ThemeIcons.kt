package com.bergenproduction.pilltracker.core.ui.designsystem.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.bergenproduction.pilltracker.core.ui.R

object ThemeIcons {
    val Add = Icons.Rounded.Add
    val Close = Icons.Rounded.Close
    val Support @Composable get() = painterResource(R.drawable.outline_support_agent_24)
    val Calculate @Composable get() = painterResource(R.drawable.outline_calculate_24)
    val Book @Composable get() = painterResource(R.drawable.outline_book_24)
    val Language @Composable get() = painterResource(R.drawable.ic_language)
    val Search @Composable get() = painterResource(R.drawable.ic_search)
    val Notes @Composable get() = painterResource(R.drawable.ic_add_notes)
    val Delete @Composable get() = painterResource(R.drawable.ic_delete)
}