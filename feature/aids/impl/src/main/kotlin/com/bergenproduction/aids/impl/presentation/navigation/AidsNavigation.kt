package com.bergenproduction.aids.impl.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bergenproduction.aids.impl.presentation.add.AddPreparationsRoute
import com.bergenproduction.aids.impl.presentation.add.navigateToDialogs
import com.bergenproduction.aids.impl.presentation.edit.EditPreparationRoute
import com.bergenproduction.aids.impl.presentation.edit.navigateToEditPreparation
import com.bergenproduction.aids.impl.presentation.info.PreparationInfoRoute
import com.bergenproduction.aids.impl.presentation.info.navigateToPreparationInfo
import com.bergenproduction.aids.impl.presentation.list.PreparationsListRoute
import com.bergenproduction.aids.impl.presentation.qna.QuestionsRoute
import com.bergenproduction.aids.impl.presentation.qna.navigateToQuestions
import com.bergenprodution.aids.api.navigation.AddPreparationNavRoute
import com.bergenprodution.aids.api.navigation.AidsGraphNavRoute
import com.bergenprodution.aids.api.navigation.AidsListNavRoute
import com.bergenprodution.aids.api.navigation.EditPreparationsNavRoute
import com.bergenprodution.aids.api.navigation.PreparationInfoNavRoute
import com.bergenprodution.aids.api.navigation.QuestionsNavRoute

fun NavGraphBuilder.preparationsListGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<AidsGraphNavRoute>(startDestination = AidsListNavRoute) {
        composable<AidsListNavRoute>(
            // region transition anim
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
            // endregion transition anim
        ) {
            PreparationsListRoute(
                onAddPrepPress = { id, name ->
                    navController.navigateToDialogs(id, name)
                },
                onCardPressed = {
                    navController.navigateToPreparationInfo(
                        id = it.id,
                        aidId = it.aidId,
                        name = it.name,
                        dosage = it.dosage,
                        expiration = it.expiration.time,
                        recommendations = it.recommendations,
                        past = it.past,
                        days = it.days,
                        months = it.months,
                        years = it.years,
                        type = it.type
                    )
                },
                onQuestionsPressed = navController::navigateToQuestions,
                modifier = modifier
            )
        }
        composable<AddPreparationNavRoute>(
            // region transition anim
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
            // endregion transition anim
        ) {
            AddPreparationsRoute(
                onDismiss = navController::navigateUp,
                modifier = modifier
            )
        }
        composable<PreparationInfoNavRoute>(
            // region transition anim
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
            // endregion transition anim
        ) {
            PreparationInfoRoute(
                onDismiss = navController::navigateUp,
                onEditPress = { id, aidId, name, dose, date, recs, type ->
                    navController.navigateToEditPreparation(id, aidId, name, dose, date, recs, type)
                },
                modifier = modifier
            )
        }
        composable<EditPreparationsNavRoute>(
            // region transition anim
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
            // endregion transition anim
        ) {
            EditPreparationRoute(
                onDismiss = navController::navigateUp,
                modifier = modifier
            )
        }
        composable<QuestionsNavRoute>(
            // region transition anim
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
            // endregion transition anim
        ) {
            QuestionsRoute(
                onBackPress = navController::navigateUp,
                modifier = modifier
            )
        }
    }
}