@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)

package com.bergenproduction.aids.impl.presentation.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bergenproduction.aids.impl.R
import com.bergenproduction.aids.impl.presentation.list.components.AddAidDialog
import com.bergenproduction.aids.impl.presentation.list.components.DeleteDialog
import com.bergenproduction.aids.impl.presentation.list.components.EditAidDialog
import com.bergenproduction.aids.impl.presentation.list.components.PreparationsListTopAppBar
import com.bergenproduction.aids.impl.presentation.models.PreparationUI
import com.bergenproduction.pilltracker.core.ui.designsystem.components.LongPressableTab
import com.bergenproduction.pilltracker.core.ui.designsystem.components.PreparationCard
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.PillTrackerTheme
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.ThemeIcons
import kotlin.math.absoluteValue

@Composable
internal fun PreparationsListRoute(
    onAddPrepPress: (Int, String) -> Unit,
    onCardPressed: (PreparationUI) -> Unit,
    onQuestionsPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<PreparationsListViewModel>()
    PreparationsListScreen(
        viewModel = viewModel,
        onAddPrepPress = onAddPrepPress,
        onCardPressed = onCardPressed,
        onQuestionsPressed = onQuestionsPressed,
        modifier = modifier
    )
}

@Composable
internal fun PreparationsListScreen(
    viewModel: PreparationsListViewModel,
    onAddPrepPress: (Int, String) -> Unit,
    onCardPressed: (PreparationUI) -> Unit,
    onQuestionsPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropDownMenu by remember { mutableIntStateOf(-1) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(null) {
        viewModel.init()
    }

    Scaffold(
        topBar = {
            PreparationsListTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                searchState = (uiState.value as? UiState.Idle)?.searchState,
                onSearchValueChange = viewModel::setSearchQuery,
                onSearchDisableClick = { viewModel.enableSearch(false) },
                onSearchEnableClick = { viewModel.enableSearch(true) },
                onQuestionsPressed = onQuestionsPressed,
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top),
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val state = uiState.value) {
                is UiState.Error -> Unit
                UiState.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                is UiState.Idle -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        PrimaryScrollableTabRow(
                            selectedTabIndex = state.aids.indexOfFirst { it.selected }.absoluteValue,
                            modifier = Modifier.fillMaxWidth(),
                            edgePadding = 0.dp,
                            divider = {}
                        ) {
                            state.aids.forEach {
                                LongPressableTab(
                                    it.selected,
                                    onClick = { viewModel.getPreparations(it.id) },
                                    onLongPress = { touchOffset ->
                                        offset = touchOffset
                                        showDropDownMenu = it.id
                                    },
                                    text = { Text(text = it.name) }
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = showDropDownMenu >= 0,
                            onDismissRequest = { showDropDownMenu = -1 },
                            offset = DpOffset(
                                x = with(LocalDensity.current) { offset.x.toDp() },
                                y = with(LocalDensity.current) { offset.y.toDp() }
                            )
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.edit)) },
                                onClick = {
                                    viewModel.showEditAidDialog(showDropDownMenu)
                                    showDropDownMenu = -1
                                },
                                leadingIcon = { Icon(Icons.Rounded.Edit, null) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.delete)) },
                                onClick = {
                                    viewModel.showDeleteAidDialog(showDropDownMenu)
                                    showDropDownMenu = -1
                                },
                                enabled = state.aids.size > 1,
                                leadingIcon = { Icon(ThemeIcons.Delete, null) }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        AnimatedContent(
                            targetState = state.preparations.isNotEmpty(),
                            label = "anim"
                        ) { target ->
                            if (target) {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(state.preparations, key = { it.id }) { preparationUI ->
                                        PreparationCard(
                                            preparationId = preparationUI.id,
                                            onDeletePress = { id ->
                                                viewModel.showDeletePreparationDialog(id)
                                            },
                                            onClick = { onCardPressed.invoke(preparationUI) },
                                            aidId = preparationUI.aidId,
                                            name = preparationUI.name,
                                            dosage = preparationUI.dosage,
                                            expiration = preparationUI.expiration,
                                            recommendations = preparationUI.recommendations,
                                            past = preparationUI.past,
                                            days = preparationUI.days,
                                            months = preparationUI.months,
                                            years = preparationUI.years,
                                            type = preparationUI.type.map { it.ordinal },
                                            modifier = Modifier.animateItem(),
                                        )
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        stringResource(R.string.add_preparation_description),
                                        modifier = Modifier.align(
                                            Alignment.Center
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        SmallFloatingActionButton(
                            onClick = { viewModel.showAddAidDialog() },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 8.dp)
                        ) {
                            Icon(ThemeIcons.Notes, null)
                        }
                        LargeFloatingActionButton(
                            onClick = {
                                onAddPrepPress.invoke(
                                    state.aids.first { it.selected }.id,
                                    state.aids.first { it.selected }.name,
                                )
                            }
                        ) {
                            Icon(ThemeIcons.Add, null, modifier = Modifier.size(36.dp))
                        }
                    }


                    when (val dialogState = state.dialogState) {
                        is DialogState.AddAid -> AddAidDialog(
                            onDismissRequest = { viewModel.dismissDialog() },
                            onAddRequest = {
                                viewModel.addAid(it)
                                viewModel.dismissDialog()
                            }
                        )

                        is DialogState.DeleteAid -> DeleteDialog(
                            onDismissRequest = {
                                viewModel.dismissDialog()
                            },
                            onConfirmRequest = {
                                viewModel.deleteAid(dialogState.aidId)
                                viewModel.dismissDialog()
                            },
                            title = stringResource(R.string.delete_aid),
                            body = stringResource(R.string.delete_aid_description)
                        )

                        is DialogState.DeletePreparation -> DeleteDialog(
                            onDismissRequest = {
                                viewModel.dismissDialog()
                            },
                            onConfirmRequest = {
                                viewModel.deletePreparation(dialogState.preparationId)
                                viewModel.dismissDialog()
                            },
                            title = stringResource(R.string.delete_preparation),
                            body = stringResource(R.string.delete_preparation_description)
                        )

                        is DialogState.EditAid -> EditAidDialog(
                            initialName = state.aids.first { it.id == dialogState.aidId }.name,
                            onDismissRequest = { viewModel.dismissDialog() },
                            onEditRequest = {
                                viewModel.editAidName(dialogState.aidId, it)
                                viewModel.dismissDialog()
                            }
                        )

                        null -> Unit
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
private fun PreparationsListPreview() {
    PillTrackerTheme {
        AddAidDialog({}, {})
    }
}

@Preview()
@Composable
private fun PreparationsListPreview2() {
    PillTrackerTheme {
        DeleteDialog(
            {},
            {},
            "Удалить?",
            "Препарат будет удален из списка. Убедитесь, что Вы точно утилизировали препарат."
        )
    }
}
