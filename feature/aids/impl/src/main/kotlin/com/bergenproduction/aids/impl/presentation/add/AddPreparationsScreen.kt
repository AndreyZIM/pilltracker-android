@file:OptIn(ExperimentalMaterial3Api::class)

package com.bergenproduction.aids.impl.presentation.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bergenproduction.aids.impl.R
import com.bergenproduction.aids.impl.presentation.models.PreparationType
import com.bergenproduction.pilltracker.core.ui.designsystem.components.FlowRow
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.PillTrackerTheme
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.ThemeIcons
import com.bergenprodution.aids.api.navigation.AddPreparationNavRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow


internal object PreparationsDefaults {
    val typeStringMap = mapOf(
        PreparationType.Antiviral to R.string.preparation_type_antiviral,
        PreparationType.Antibiotic to R.string.preparation_type_antibiotic,
        PreparationType.Sedative to R.string.preparation_type_sedative,
        PreparationType.Painkiller to R.string.preparation_type_painkiller,
        PreparationType.AntiInflammatory to R.string.preparation_type_antiinflammatory,
        PreparationType.Laxative to R.string.preparation_type_laxative,
        PreparationType.Mucolytic to R.string.preparation_type_mucolytic,
        PreparationType.Antihistamine to R.string.preparation_type_antihistamine,
        PreparationType.Hypnotic to R.string.preparation_type_hypnotic,
        PreparationType.DietarySupplement to R.string.preparation_type_dietarysupplement,
        PreparationType.Vitamin to R.string.preparation_type_vitamin,
        PreparationType.HeatLower to R.string.preparation_type_heatlower,
        PreparationType.BloodBoiler to R.string.preparation_type_bloodboiler,
        PreparationType.KOK to R.string.preparation_type_kok,
    )
}

internal fun NavController.navigateToDialogs(
    aidId: Int,
    aidName: String,
    navOptions: NavOptions? = null
) {
    this.navigate(AddPreparationNavRoute(aidId, aidName), navOptions)
}


@Composable
fun AddPreparationsRoute(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<AddPreparationViewModel>()

    AddPreparationsScreen(
        onDismiss = onDismiss,
        modifier = modifier,
        nameFieldState = viewModel.nameFieldState,
        doseFieldState = viewModel.doseFieldState,
        dateFieldState = viewModel.dateFieldState,
        recommendationsFieldState = viewModel.recommendationsFieldState,
        canContinue = viewModel.canContinue,
        selectedTypes = viewModel.selectedTypes,
        onAddClick = viewModel::addPreparation,
        onNameChange = viewModel::rememberName,
        onDoseChange = viewModel::rememberDose,
        onRecommendationChange = viewModel::rememberRecommendation,
        onDateChange = viewModel::rememberDate,
        onTypeSelect = viewModel::selectType,
    )
}

@Composable
fun AddPreparationsScreen(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    nameFieldState: StateFlow<FieldState>,
    doseFieldState: StateFlow<FieldState>,
    dateFieldState: StateFlow<FieldState>,
    recommendationsFieldState: StateFlow<FieldState>,
    canContinue: StateFlow<Boolean>,
    selectedTypes: StateFlow<MutableSet<Int>>,
    onAddClick: (Boolean, Boolean, Boolean) -> Unit,
    onNameChange: (String) -> Unit,
    onDoseChange: (String) -> Unit,
    onRecommendationChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onTypeSelect: (Int) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }
    var haveToBeConsumedAtMorning by remember { mutableStateOf(false) }
    var haveToBeConsumedAtNoon by remember { mutableStateOf(false) }
    var haveToBeConsumedAtEvening by remember { mutableStateOf(false) }

    val nameState = nameFieldState.collectAsStateWithLifecycle()
    val doseState = doseFieldState.collectAsStateWithLifecycle()
    val dateState = dateFieldState.collectAsStateWithLifecycle()
    val recommendationState = recommendationsFieldState.collectAsStateWithLifecycle()
    val canContinue = canContinue.collectAsStateWithLifecycle()
    val selectedTypes = selectedTypes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавление", fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onDismiss) {
                        Icon(Icons.Default.Close, null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onAddClick(
                                haveToBeConsumedAtMorning,
                                haveToBeConsumedAtNoon,
                                haveToBeConsumedAtEvening,
                            )
                            onDismiss.invoke()
                        },
                        enabled = canContinue.value
                    ) {
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
                    onValueChange = onNameChange,
                    label = { Text("Название") },
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
                        if (it.isDigitsOnly()) onDoseChange(it)
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
                    onValueChange = onRecommendationChange,
                    label = { Text(stringResource(R.string.recomendations_for_taking)) },
                    isError = recommendationState.value.isError(),
                    supportingText = {
                        if (recommendationState.value.error.isNotEmpty()) Text(text = recommendationState.value.error)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            item { Text(text = "Время приема") }
            item {
                Row {
                    Checkbox(
                        checked = haveToBeConsumedAtMorning,
                        onCheckedChange = { haveToBeConsumedAtMorning = it })
                    Text("Утром", modifier = Modifier.align(Alignment.CenterVertically))
                }

            }
            item {
                Row {
                    Checkbox(
                        checked = haveToBeConsumedAtNoon,
                        onCheckedChange = { haveToBeConsumedAtNoon = it })
                    Text("День", modifier = Modifier.align(Alignment.CenterVertically))
                }

            }
            item {
                Row {
                    Checkbox(
                        checked = haveToBeConsumedAtEvening,
                        onCheckedChange = { haveToBeConsumedAtEvening = it })
                    Text("Вечер", modifier = Modifier.align(Alignment.CenterVertically))
                }
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
                                    Icon(ThemeIcons.Check, null)
                                }
                            },
                            onClick = {
                                onTypeSelect(it.ordinal)
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
                if (dateResult != null) onDateChange(dateResult)
                showPicker = false
            },
            onDismiss = { showPicker = false }
        )
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview(device = Devices.PIXEL_3)
@Composable
private fun Preview() {
    PillTrackerTheme {
        AddPreparationsScreen(
            {},
            modifier = Modifier.fillMaxSize(),
            nameFieldState = MutableStateFlow<FieldState>(FieldState("")),
            doseFieldState = MutableStateFlow<FieldState>(FieldState("")),
            dateFieldState = MutableStateFlow<FieldState>(FieldState("")),
            recommendationsFieldState = MutableStateFlow<FieldState>(FieldState("")),
            canContinue = MutableStateFlow<Boolean>(true),
            selectedTypes = MutableStateFlow<MutableSet<Int>>(mutableSetOf(0, 2, 3)),
            {_,_,_ ->},
            {},
            {},
            {},
            {},
            {}
        )
    }
}

