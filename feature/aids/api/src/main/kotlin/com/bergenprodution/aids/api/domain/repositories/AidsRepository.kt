package com.bergenprodution.aids.api.domain.repositories

import com.bergenprodution.aids.api.domain.models.Aid
import kotlinx.coroutines.flow.Flow

interface AidsRepository {

    fun getAidsFlow(): Flow<List<Aid>>

    suspend fun addAid(name: String)

    suspend fun deletePreparation(id: Int, aidId: Int)

    suspend fun deleteAid(id: Int)

    suspend fun editAid(id: Int, name: String)
}