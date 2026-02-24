package com.bergenprodution.aids.api.navigation

import com.bergenproduction.common.navigation.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
object AidsGraphNavRoute : NavigationRoute

@Serializable
object AidsListNavRoute : NavigationRoute

@Serializable
data class PreparationInfoNavRoute(
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
) : NavigationRoute

@Serializable
data class AddPreparationNavRoute(val aidId: Int, val aidName: String) : NavigationRoute

@Serializable
data class EditPreparationsNavRoute(
    val id: Int,
    val aidId: Int,
    val name: String,
    val dosage: Int,
    val expiration: Long,
    val recommendations: String,
    val type: String
) : NavigationRoute

@Serializable
object QuestionsNavRoute : NavigationRoute