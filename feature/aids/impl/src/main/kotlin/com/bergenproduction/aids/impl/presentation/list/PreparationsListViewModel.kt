@file:OptIn(ExperimentalCoroutinesApi::class)

package com.bergenproduction.aids.impl.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bergenproduction.aids.impl.presentation.models.AidUI
import com.bergenproduction.aids.impl.presentation.models.PreparationUI
import com.bergenproduction.aids.impl.utils.toUi
import com.bergenproduction.reminders.api.RemindersManager
import com.bergenprodution.aids.api.domain.repositories.AidsRepository
import com.bergenprodution.aids.api.domain.repositories.PreparationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PreparationsListViewModel @Inject constructor(
    private val preparationRepository: PreparationRepository,
    private val aidsRepository: AidsRepository,
    private val remindersManager: RemindersManager
) : ViewModel() {

    private val _aidsList: MutableStateFlow<List<AidUI>> = MutableStateFlow(emptyList())

    private val _dialogState: MutableStateFlow<DialogState?> = MutableStateFlow(null)

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(
        SearchState(
            enabled = false,
            query = ""
        )
    )

    val uiState: StateFlow<UiState> = combine(
        _dialogState,
        _searchState,
        _aidsList
            .flatMapLatest { aids ->
                val selectedAid = aids.first { it.selected }
                preparationRepository.getPreparations(selectedAid.id)
            }
    ) { dialog, searchState, preparations ->
        val filteredPreparations = preparations
            .map { it.toUi() }
            .filter {
                if (searchState.enabled && searchState.query.isNotEmpty())
                    it.name
                        .lowercase()
                        .contains(searchState.query.lowercase())
                else true
            }
        UiState.Idle(
            searchState = searchState,
            preparations = filteredPreparations,
            aids = _aidsList.value,
            dialogState = dialog
        ) as UiState
    }
        .catch { this.emit(UiState.Error(it.message.orEmpty())) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UiState.Loading
        )

    private var fetchingJob: Job? = null

    fun init() {
        fetchingJob = aidsRepository.getAidsFlow()
            .onEach { aids ->
                _aidsList.update { oldList ->
                    val selectedIndex = oldList.indexOfFirst { it.selected }
                    aids.mapIndexed { index, aid ->
                        aid.toUi(selected = (if (selectedIndex < 0) selectedIndex else 0) == index)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun getPreparations(aidId: Int) {
        _aidsList.update {
            val selectedAid = it.firstOrNull { aid -> aid.id == aidId }
            selectedAid?.id?.let { selectedAidId ->
                it.map { aid ->
                    aid.copy(selected = aid.id == selectedAidId)
                }
            } ?: it
        }
    }

    fun enableSearch(enabled: Boolean) {
        _searchState.update { SearchState(enabled, "") }
    }

    fun setSearchQuery(query: String) {
        _searchState.update { it.copy(query = query) }
    }

    fun addAid(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            aidsRepository.addAid(name)
        }
    }

    fun editAidName(aidId: Int, name: String) = viewModelScope.launch(Dispatchers.IO) {
        aidsRepository.editAid(aidId, name)
    }

    fun deleteAid(aidId: Int) {
        val currentState = uiState.value
        if (currentState is UiState.Idle) {
            viewModelScope.launch(Dispatchers.IO) {
                currentState.preparations
                    .filter { aidId == it.aidId }
                    .forEach { remindersManager.deleteReminder(it.id) }
                aidsRepository.deleteAid(aidId)
            }
        }
    }

    fun deletePreparation(preparationId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            preparationRepository.deletePreparation(preparationId)
            remindersManager.deleteReminder(preparationId)
        }
    }

    fun showDeletePreparationDialog(id: Int) {
        _dialogState.update { DialogState.DeletePreparation(id) }
    }

    fun showDeleteAidDialog(id: Int) {
        _dialogState.update { DialogState.DeleteAid(id) }
    }

    fun showEditAidDialog(aidId: Int) {
        _dialogState.update { DialogState.EditAid(aidId) }
    }

    fun showAddAidDialog() {
        _dialogState.update { DialogState.AddAid }
    }

    fun dismissDialog() {
        _dialogState.update { null }
    }
}

internal sealed class UiState {

    data object Loading : UiState()

    data class Idle(
        val searchState: SearchState,
        val preparations: List<PreparationUI>,
        val aids: List<AidUI>,
        val dialogState: DialogState? = null
    ) : UiState()

    data class Error(val errorMessage: String) : UiState()
}

internal sealed class DialogState {
    data class DeleteAid(val aidId: Int) : DialogState()
    data class DeletePreparation(val preparationId: Int) : DialogState()
    data object AddAid : DialogState()
    data class EditAid(val aidId: Int) : DialogState()
}

internal data class SearchState(
    val enabled: Boolean,
    val query: String,
)