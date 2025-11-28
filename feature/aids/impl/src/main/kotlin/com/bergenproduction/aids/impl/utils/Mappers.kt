package com.bergenproduction.aids.impl.utils

import com.bergenproduction.aids.impl.presentation.models.AidUI
import com.bergenproduction.aids.impl.presentation.models.PreparationType
import com.bergenproduction.aids.impl.presentation.models.PreparationUI
import com.bergenprodution.aids.api.domain.models.Aid
import com.bergenprodution.aids.api.domain.models.Preparation
import java.time.Period
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

fun Preparation.toUi(): PreparationUI {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = Date(System.currentTimeMillis())
    cal2.time = expiration

    val startDate = Date(System.currentTimeMillis())
    val endDate = expiration

    val offsetStart = startDate.toInstant().atZone(ZoneId.systemDefault())
    val offsetEnd = endDate.toInstant().atZone(ZoneId.systemDefault())

    val period = Period.between(offsetStart.toLocalDate(), offsetEnd.toLocalDate())

    return PreparationUI(
        id,
        aidId,
        name,
        dosage,
        expiration,
        recommendations,
        System.currentTimeMillis() >= expiration.time,
        period.days,
        period.months,
        period.years,
        type.map { type -> PreparationType.entries.first { it.ordinal == type } },
        haveToBeConsumedAtMorning,
        haveToBeConsumedAtNoon,
        haveToBeConsumedAtEvening,
    )
}

fun Aid.toUi(selected: Boolean = false) = AidUI(id = id, name = name, selected = selected)