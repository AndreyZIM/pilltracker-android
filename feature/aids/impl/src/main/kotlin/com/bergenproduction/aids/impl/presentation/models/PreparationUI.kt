package com.bergenproduction.aids.impl.presentation.models

import java.util.Date

data class PreparationUI(
    val id: Int,
    val aidId: Int,
    val name: String,
    val dosage: Int,
    val expiration: Date,
    val recommendations: String,
    val past: Boolean,
    val days: Int,
    val months: Int,
    val years: Int,
    val type: List<PreparationType>,
    val haveToBeConsumedAtMorning: Boolean,
    val haveToBeConsumedAtNoon: Boolean,
    val haveToBeConsumedAtEvening: Boolean,
)

enum class PreparationType {
    Antiviral,
    Antibiotic,
    Sedative,
    Painkiller,
    AntiInflammatory,
    Laxative,
    Mucolytic,
    Antihistamine,
    Hypnotic,
    DietarySupplement,
    Vitamin,
    HeatLower,
    BloodBoiler,
    KOK
}