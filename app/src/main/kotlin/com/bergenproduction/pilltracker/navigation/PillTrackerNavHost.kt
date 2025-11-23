package com.bergenproduction.pilltracker.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bergenproduction.common.navigation.NavigationRoute
import com.bergenproduction.pilltracker.ui.PillTrackerAppState
import com.kiracooper.pilltracker.presentation.PillTrackerAppState
import com.kiracooper.pilltracker.presentation.features.add.AddPreparationViewModel
import com.kiracooper.pilltracker.presentation.features.add.AddPreparationsRoute
import com.kiracooper.pilltracker.presentation.features.barrier.BarrierRoute
import com.kiracooper.pilltracker.presentation.features.edit.EditPreparationRoute
import com.kiracooper.pilltracker.presentation.features.edit.EditPreparationViewModel
import com.kiracooper.pilltracker.presentation.features.info.PreparationInfoRoute
import com.kiracooper.pilltracker.presentation.features.info.PreparationInfoViewModel
import com.kiracooper.pilltracker.presentation.features.list.PreparationsListRoute
import com.kiracooper.pilltracker.presentation.features.list.PreparationsListViewModel
import com.kiracooper.pilltracker.presentation.features.list.preparationsListGraph
import com.kiracooper.pilltracker.presentation.features.qna.QuestionsRoute
import com.kiracooper.pilltracker.presentation.features.settings.SettingsRoute
import com.kiracooper.pilltracker.presentation.models.PreparationType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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