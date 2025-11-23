package com.bergenproduction.pilltracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiracooper.pilltracker.domain.repositories.PreparationRepository
import com.kiracooper.pilltracker.presentation.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStore,
    private val preparationRepository: PreparationRepository
): ViewModel() {

    private val _isInitiated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isPermissionGranted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInitiated = combine(
        _isInitiated,
        _isPermissionGranted
    ) { initiated, permission ->
        initiated && permission
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(3000L),
            false
        )

    init {
        viewModelScope.launch {
            if (!dataStore.isFirstRun()) {
                preparationRepository.addAid("Основная")
                dataStore.triggerFirstRun()
            }
            _isInitiated.update { true }
        }
    }

    fun isPermissionGranted(isGranted: Boolean) {
        _isPermissionGranted.update {isGranted}
        if (!isGranted) {
            // TODO show rationale
        }
    }

    fun getTheme() = dataStore.getTheme()

    sealed class MainState {
        data object Initiating: MainState()
        data object Initiated: MainState()
        data object PermissionError: MainState()
    }
}