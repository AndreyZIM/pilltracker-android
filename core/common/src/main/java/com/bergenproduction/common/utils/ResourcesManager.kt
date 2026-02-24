package com.bergenproduction.common.utils

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ResourcesManager {

    fun getString(@StringRes id: Int): String

    class Base @Inject constructor(
        @ApplicationContext private val context: Context
    ) : ResourcesManager {

        override fun getString(id: Int): String =
            context.getString(id)
    }
}