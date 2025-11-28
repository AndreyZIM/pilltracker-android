package com.bergenprodution.aids.api.domain.repositories

import com.bergenprodution.aids.api.domain.models.Preparation
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PreparationRepository {

    fun getPreparations(aidId: Int): Flow<List<Preparation>>

    fun getOnCoursePreparations(): Flow<List<Preparation>>

    suspend fun getPreparation(id: Int): Preparation

    suspend fun addPreparation(
        id: Int,
        aidId: Int,
        name: String,
        dosage: Int,
        expiration: Date,
        recommendations: String,
        type: IntArray
    ): Int

    suspend fun editPreparation(
        id: Int,
        aidId: Int,
        name: String,
        dosage: Int,
        expiration: Date,
        recommendations: String,
        type: IntArray
    )

    suspend fun deletePreparation(id: Int)
}