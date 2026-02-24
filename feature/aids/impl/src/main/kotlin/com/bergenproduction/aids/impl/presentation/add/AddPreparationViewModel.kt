package com.bergenproduction.aids.impl.presentation.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.bergenproduction.common.utils.copyAndAdd
import com.bergenproduction.common.utils.copyAndRemove
import com.bergenproduction.common.utils.toDateString
import com.bergenproduction.reminders.api.RemindersManager
import com.bergenprodution.aids.api.domain.repositories.PreparationRepository
import com.bergenprodution.aids.api.navigation.AddPreparationNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddPreparationViewModel @Inject constructor(
    private val preparationRepository: PreparationRepository,
    private val remindersManager: RemindersManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val aidId = savedStateHandle.toRoute<AddPreparationNavRoute>().aidId

    private var dateLong = -1L

    private val _nameFieldState = MutableStateFlow(FieldState("", ""))
    val nameFieldState = _nameFieldState.asStateFlow()

    private val _doseFieldState = MutableStateFlow(FieldState("", ""))
    val doseFieldState = _doseFieldState.asStateFlow()

    private val _dateFieldState = MutableStateFlow(FieldState("", ""))
    val dateFieldState = _dateFieldState.asStateFlow()

    private val _selectedTypes = MutableStateFlow(mutableSetOf<Int>())
    val selectedTypes = _selectedTypes.asStateFlow()

    private val _recommendationsFieldState = MutableStateFlow(FieldState("", ""))
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
                date.text.isNotEmpty()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000L),
            false
        )

    fun addPreparation(
        haveToBeConsumedAtMorning: Boolean,
        haveToBeConsumedAtNoon: Boolean,
        haveToBeConsumedAtEvening: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = preparationRepository.addPreparation(
                0,
                aidId,
                nameFieldState.value.text,
                doseFieldState.value.text.toInt(),
                Date(dateLong),
                recommendationsFieldState.value.text,
                selectedTypes.value.toIntArray(),
                // TODO add prep
                haveToBeConsumedAtMorning = haveToBeConsumedAtMorning,
                haveToBeConsumedAtNoon = haveToBeConsumedAtNoon,
                haveToBeConsumedAtEvening = haveToBeConsumedAtEvening
            )
            remindersManager.setReminder(id, dateLong)
        }
    }

    fun rememberDate(dateLong: Long) {
        this.dateLong = dateLong
        val text = dateLong.toDateString()
        _dateFieldState.update {
            FieldState(
                text,
                when {
                    text.isEmpty() -> "Поле не должно быть пустым"
                    else -> ""
                }
            )
        }
    }

    fun rememberName(text: String) {
        _nameFieldState.update {
            FieldState(
                text,
                when {
                    text.isEmpty() -> "Поле не должно быть пустым"
                    else -> ""
                }
            )
        }
    }

    fun rememberDose(text: String) {
        _doseFieldState.update {
            FieldState(
                text,
                when {
                    text.isEmpty() -> "Поле не должно быть пустым"
                    else -> ""
                }
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
        if (selectedTypes.value.contains(id))
            _selectedTypes.update {
                val new = it.copyAndRemove(id)
                println(new)
                new
            }
        else
            _selectedTypes.update {
                val new = it.copyAndAdd(id)
                println(new)
                new
            }
    }
}

data class FieldState(
    val text: String,
    val error: String = ""
) {
    fun isError() = error.isNotEmpty()
}