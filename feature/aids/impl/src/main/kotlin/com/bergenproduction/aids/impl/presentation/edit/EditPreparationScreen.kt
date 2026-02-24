@file:OptIn(ExperimentalMaterial3Api::class)

package com.bergenproduction.aids.impl.presentation.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bergenproduction.aids.impl.R
import com.bergenproduction.aids.impl.presentation.add.DatePickerModal
import com.bergenproduction.aids.impl.presentation.add.PreparationsDefaults
import com.bergenproduction.aids.impl.presentation.models.PreparationType
import com.bergenproduction.pilltracker.core.ui.designsystem.components.FlowRow
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.ThemeIcons
import com.bergenprodution.aids.api.navigation.EditPreparationsNavRoute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun NavController.navigateToEditPreparation(
    id: Int,
    aidId: Int,
    name: String,
    dose: Int,
    date: Long,
    recs: String,
    type: List<PreparationType>,
    navOptions: NavOptions? = null
) {
    this.navigate(
        EditPreparationsNavRoute(id, aidId, name, dose, date, recs, Json.encodeToString(type)),
        navOptions
    )
}

@Composable
internal fun EditPreparationRoute(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    EditPreparationScreen(
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
internal fun EditPreparationScreen(
    viewModel: EditPreparationViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    val nameState = viewModel.nameFieldState.collectAsStateWithLifecycle()
    val doseState = viewModel.doseFieldState.collectAsStateWithLifecycle()
    val dateState = viewModel.dateFieldState.collectAsStateWithLifecycle()
    val recommendationState = viewModel.recommendationsFieldState.collectAsStateWithLifecycle()
    val canContinue = viewModel.canContinue.collectAsStateWithLifecycle()
    val selectedTypes = viewModel.selectedTypes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editing), fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onDismiss) {
                        Icon(Icons.Default.Close, null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.addPreparation()
                            onDismiss.invoke()
                        },
                        enabled = canContinue.value) {
                        Icon(Icons.Default.Check, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = nameState.value.text,
                    onValueChange = { viewModel.rememberName(it) },
                    label = { Text(stringResource(R.string.name)) },
                    isError = nameState.value.isError(),
                    supportingText = {
                        if (nameState.value.error.isNotEmpty()) Text(text = nameState.value.error)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = doseState.value.text,
                    onValueChange = {
                        if (it.isDigitsOnly()) viewModel.rememberDose(it)
                    },
                    label = { Text(stringResource(R.string.dosage_mg)) },
                    isError = doseState.value.isError(),
                    supportingText = {
                        if (doseState.value.error.isNotEmpty()) Text(text = doseState.value.error)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
            item {
                OutlinedTextField(
                    value = dateState.value.text,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.expires_at_hint)) },
                    isError = dateState.value.isError(),
                    supportingText = {
                        if (dateState.value.error.isNotEmpty()) Text(text = dateState.value.error)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton({ showPicker = true }) {
                            Icon(Icons.Default.DateRange, null)
                        }
                    }
                )
            }
            item {
                OutlinedTextField(
                    value = recommendationState.value.text,
                    onValueChange = { viewModel.rememberRecommendation(it) },
                    label = { Text(stringResource(R.string.recomendations_for_taking)) },
                    isError = recommendationState.value.isError(),
                    supportingText = {
                        if (recommendationState.value.error.isNotEmpty()) Text(text = recommendationState.value.error)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            item { Text(text = "Тип препарата") }
            item {
                FlowRow(
                    horizontalGap = 8.dp
                ) {
                    PreparationType.entries.forEach {
                        FilterChip(
                            selected = selectedTypes.value.contains(it.ordinal),
                            label = {
                                Text(text = stringResource(PreparationsDefaults.typeStringMap[it]!!))
                            },
                            leadingIcon = {
                                AnimatedVisibility(selectedTypes.value.contains(it.ordinal)) {
                                    Icon(painter = ThemeIcons.Check, null)
                                }
                            },
                            onClick = {
                                viewModel.selectType(it.ordinal)
                            }
                        )
                    }
                }
            }
        }
    }
    if (showPicker)
        DatePickerModal(
            onDateSelected = { dateResult ->
                if (dateResult != null) viewModel.rememberDate(dateResult)
                showPicker = false
            },
            onDismiss = { showPicker = false }
        )
}
