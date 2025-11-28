package com.bergenproduction.pilltracker.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bergenproduction.common.navigation.NavigationRoute
import com.bergenproduction.pilltracker.ui.PillTrackerAppState

@Composable
fun PillTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appState: PillTrackerAppState
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.PreparationsListGraph,
        modifier = Modifier.fillMaxSize()
    ) {
        preparationsListGraph(navController, modifier)
        composable<PillTrackerNavigation.Settings> {
            SettingsRoute(
                navController::navigateUp,
                appState::setTheme,
                Modifier.fillMaxSize()
            )
        }
        composable<PillTrackerNavigation.TakingDairy> {
            BarrierRoute(
                Modifier.fillMaxSize()
            )
        }
    }
}

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(NavigationRoute.Settings, navOptions)
}

fun NavController.navigateToDairy(navOptions: NavOptions? = null) {
    this.navigate(NavigationRoute.TakingDairy, navOptions)
}