package com.bergenproduction.pilltracker.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.bergenproduction.common.navigation.NavigationRoute
import com.bergenproduction.pilltracker.R

enum class TopLevelDestinations(
    val icon: ImageVector,
    @StringRes val labelText: Int,
    val route: NavigationRoute
) {
    LIST(
        icon = Icons.Default.Check,
        labelText = R.string.aids,
        route = NavigationRoute.PreparationsList
    ),
    DAIRY(
        icon = Icons.Default.DateRange,
        labelText = R.string.dairy,
        route = NavigationRoute.TakingDairy
    ),
    SETTINGS(
        icon = Icons.Default.Settings,
        labelText = R.string.settings,
        route = NavigationRoute.Settings
    )
}