package com.bergenproduction.pilltracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.bergenproduction.pilltracker.navigation.PillTrackerNavHost
import com.bergenproduction.pilltracker.navigation.TopLevelDestinations

@Composable
fun PreparationsApp(
    appState: PillTrackerAppState,
    navController: NavHostController,
    modifier: Modifier
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination,
                modifier = Modifier,
                isVisible = appState.shouldShowBottomBar
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Bottom),
        modifier = modifier
    ) {
        val bottomPadding: Dp by animateDpAsState(
            if (appState.shouldShowBottomBar) it.calculateBottomPadding() else 0.dp
        )
        PillTrackerNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = bottomPadding
                ),
            appState = appState
        )
    }
}

@Composable
fun BottomBar(
    destinations: List<TopLevelDestinations>,
    onNavigateToDestination: (TopLevelDestinations) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
    ) {
        NavigationBar(modifier = modifier) {
            destinations.forEach { destination ->
                val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = { Icon(imageVector = destination.icon, contentDescription = null) },
                    label = { Text(text = stringResource(destination.labelText)) },
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destinations: TopLevelDestinations) =
    this?.hierarchy?.any {
        it.route?.contains(destinations.name, true) ?: false
    } ?: false