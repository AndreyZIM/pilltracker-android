package com.bergenprodution.aids.api.navigation

import com.bergenproduction.common.navigation.NavigationRoute
import kotlinx.serialization.Serializable

sealed interface Aids : NavigationRoute {
    @Serializable
    object AidsGraph : Aids

    @Serializable
    object AidsList : Aids

    @Serializable
    data class PreparationInfo(
        val id: Int,
        val aidId: Int,
        val name: String,
        val dosage: Int,
        val expiration: Long,
        val recommendations: String,
        val type: String,
        val past: Boolean,
        val days: Int,
        val months: Int,
        val years: Int,
    ) : Aids

    @Serializable
    data class AddPreparation(val aidId: Int, val aidName: String) : Aids

    @Serializable
    data class EditPreparations(
        val id: Int,
        val aidId: Int,
        val name: String,
        val dosage: Int,
        val expiration: Long,
        val recommendations: String,
        val type: String
    ) : Aids

    @Serializable
    object Questions : Aids
}