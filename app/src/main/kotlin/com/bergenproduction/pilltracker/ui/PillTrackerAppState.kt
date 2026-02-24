package com.bergenproduction.pilltracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.bergenproduction.common.navigation.NavigationRoute
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.AppThemeColorScheme
import com.bergenproduction.pilltracker.navigation.TopLevelDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Composable
fun rememberAppState(
    themeColorScheme: AppThemeColorScheme,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, coroutineScope) {
    PillTrackerAppState(themeColorScheme, navController, coroutineScope)
}

@Stable
class PillTrackerAppState(
    themeColorScheme: AppThemeColorScheme,
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {

    private val _currentTheme: MutableStateFlow<AppThemeColorScheme> =
        MutableStateFlow(themeColorScheme)
    val currentTheme: StateFlow<AppThemeColorScheme> = _currentTheme.asStateFlow()

    val currentDestination
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination


    val topLevelDestinations: List<TopLevelDestinations> = TopLevelDestinations.entries.toList()

    private val topLevelRoutes = listOf(
        NavigationRoute.PreparationsList,
        NavigationRoute.TakingDairy,
        NavigationRoute.Settings,
    )

    val shouldShowBottomBar: Boolean
        @Composable get() = topLevelRoutes.any { currentDestination?.route == it.javaClass.canonicalName }

    fun navigateToTopLevelDestination(topLevelDestinations: TopLevelDestinations) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = false
//                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        if (navController.currentBackStackEntry?.destination?.route != topLevelDestinations.route.javaClass.canonicalName) {

            when (topLevelDestinations) {
                TopLevelDestinations.LIST -> navController.navigateToPreparationsGraph(
                    topLevelNavOptions
                )

                TopLevelDestinations.DAIRY -> navController.navigateToDairy(topLevelNavOptions)
                TopLevelDestinations.SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
            }
        }
    }

    fun setTheme(themeColorScheme: AppThemeColorScheme) {
        _currentTheme.update { themeColorScheme }
    }
}