package com.bergenproduction.aids.impl.presentation.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.bergenproduction.aids.impl.presentation.models.PreparationType
import com.bergenprodution.aids.api.domain.repositories.PreparationRepository
import com.bergenprodution.aids.api.navigation.PreparationInfoNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class PreparationInfoViewModel @Inject constructor(
    private val preparationRepository: PreparationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: Int = savedStateHandle.toRoute<PreparationInfoNavRoute>().id
    val aidId: Int = savedStateHandle.toRoute<PreparationInfoNavRoute>().aidId
    val name: String = savedStateHandle.toRoute<PreparationInfoNavRoute>().name
    val dosage: Int = savedStateHandle.toRoute<PreparationInfoNavRoute>().dosage
    val expiration: Long =
        savedStateHandle.toRoute<PreparationInfoNavRoute>().expiration
    val recommendations: String =
        savedStateHandle.toRoute<PreparationInfoNavRoute>().recommendations
    val past: Boolean = savedStateHandle.toRoute<PreparationInfoNavRoute>().past
    val days: Int = savedStateHandle.toRoute<PreparationInfoNavRoute>().days
    val months: Int = savedStateHandle.toRoute<PreparationInfoNavRoute>().months
    val years: Int = savedStateHandle.toRoute<PreparationInfoNavRoute>().years
    val type: List<PreparationType> =
        Json.decodeFromString<List<PreparationType>>(savedStateHandle.toRoute<PreparationInfoNavRoute>().type)

    private val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    val expirationDate = format.format(Date(expiration))
}