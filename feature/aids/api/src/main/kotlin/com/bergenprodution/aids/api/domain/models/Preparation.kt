package com.bergenprodution.aids.api.domain.models

import java.util.Date

data class Preparation(
    val id: Int,
    val aidId: Int,
    val name: String,
    val dosage: Int,
    val expiration: Date,
    val recommendations: String,
    val type: List<Int>,
    val haveToBeConsumedAtMorning: Boolean,
    val haveToBeConsumedAtNoon: Boolean,
    val haveToBeConsumedAtEvening: Boolean,
)
