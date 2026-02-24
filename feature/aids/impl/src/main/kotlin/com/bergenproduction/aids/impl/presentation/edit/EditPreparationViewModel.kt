package com.bergenproduction.aids.impl.presentation.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bergenproduction.aids.impl.R
import com.bergenproduction.aids.impl.presentation.add.FieldState
import com.bergenproduction.aids.impl.presentation.models.PreparationType
import com.bergenproduction.common.utils.ResourcesManager
import com.bergenproduction.common.utils.copyAndAdd
import com.bergenproduction.common.utils.copyAndRemove
import com.bergenproduction.common.utils.toDateString
import com.bergenproduction.reminders.api.RemindersManager
import com.bergenprodution.aids.api.domain.repositories.PreparationRepository
import com.bergenprodution.aids.api.navigation.EditPreparationsNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditPreparationViewModel @Inject constructor(
    private val preparationRepository: PreparationRepository,
    private val remindersManager: RemindersManager,
    private val savedStateHandle: SavedStateHandle,
    private val resourcesManager: ResourcesManager,
) : ViewModel() {

    private val id = savedStateHandle.toRoute<EditPreparationsNavRoute>().id
    private val aidId = savedStateHandle.toRoute<EditPreparationsNavRoute>().aidId
    private val oldName = savedStateHandle.toRoute<EditPreparationsNavRoute>().name
    private val oldDose = savedStateHandle.toRoute<EditPreparationsNavRoute>().dosage
    private val oldDate = savedStateHandle.toRoute<EditPreparationsNavRoute>().expiration
    private val oldRecs = savedStateHandle.toRoute<EditPreparationsNavRoute>().recommendations

    private val oldTypes =
        Json.decodeFromString<List<PreparationType>>(savedStateHandle.toRoute<EditPreparationsNavRoute>().type)

    private var dateLong = oldDate

    private val _nameFieldState = MutableStateFlow(FieldState(oldName, ""))
    val nameFieldState = _nameFieldState.asStateFlow()

    private val _doseFieldState = MutableStateFlow(FieldState(oldDose.toString(), ""))
    val doseFieldState = _doseFieldState.asStateFlow()

    private val _dateFieldState = MutableStateFlow(FieldState(oldDate.toDateString(), ""))
    val dateFieldState = _dateFieldState.asStateFlow()

    private val _selectedTypes = MutableStateFlow(oldTypes.map { it.ordinal }.toMutableSet())
    val selectedTypes = _selectedTypes.asStateFlow()

    private val _recommendationsFieldState = MutableStateFlow(FieldState(oldRecs, ""))
    val recommendationsFieldState = _recommendationsFieldState.asStateFlow()
    val canContinue = combine(
        nameFieldState,
        doseFieldState,
        dateFieldState,
        recommendationsFieldState,
    ) { name, dose, date, recs ->
        !name.isError() &&
                !dose.isError() &&
                !date.isError() &&
                !recs.isError() &&
                name.text.isNotEmpty() &&
                dose.text.isNotEmpty() &&
                date.text.isNotEmpty() &&
                (name.text != oldName || dose.text.toInt() != oldDose || dateLong != oldDate || recs.text != oldRecs)
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000L),
            false
        )

    fun addPreparation() {
        viewModelScope.launch(Dispatchers.IO) {
            preparationRepository.editPreparation(
                id = id,
                aidId = aidId,
                name = nameFieldState.value.text,
                dosage = doseFieldState.value.text.toInt(),
                expiration = Date(dateLong),
                recommendations = recommendationsFieldState.value.text,
                type = selectedTypes.value.toIntArray(),
                haveToBeConsumedAtMorning = false,
                haveToBeConsumedAtNoon = false,
                haveToBeConsumedAtEvening = false
            )
            if (dateLong != oldDate) {
                remindersManager.deleteReminder(id)
                remindersManager.setReminder(id, dateLong)
            }
        }
    }

    fun rememberDate(dateLong: Long) {
        this.dateLong = dateLong
        val text = dateLong.toDateString()
        _dateFieldState.update {
            FieldState(
                text = text,
                error = if (text.isEmpty()) resourcesManager.getString(R.string.field_should_not_be_empty) else ""
            )
        }
    }

    fun rememberName(text: String) {
        _nameFieldState.update {
            FieldState(
                text = text,
                error = if (text.isEmpty()) resourcesManager.getString(R.string.field_should_not_be_empty) else ""
            )
        }
    }

    fun rememberDose(text: String) {
        _doseFieldState.update {
            FieldState(
                text = text,
                error = if (text.isEmpty()) resourcesManager.getString(R.string.field_should_not_be_empty) else ""
            )
        }
    }

    fun rememberRecommendation(text: String) {
        _recommendationsFieldState.update {
            FieldState(
                text,
                ""
            )
        }
    }

    fun selectType(id: Int) {
        if (selectedTypes.value.contains(id)) _selectedTypes.update { it.copyAndRemove(id) }
        else _selectedTypes.update { it.copyAndAdd(id) }
    }
}