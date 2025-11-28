@file:OptIn(ExperimentalMaterial3Api::class)

package com.bergenproduction.aids.impl.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    title: String,
    body: String,
    modifier: Modifier = Modifier.Companion
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        ElevatedCard(
            modifier = Modifier.Companion.wrapContentSize(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.Companion.padding(24.dp)) {
                Text(
                    text = title,
                    modifier = Modifier.Companion,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = body,
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(modifier = Modifier.align(Alignment.Companion.End)) {
                    TextButton(onDismissRequest) {
                        Text(text = "Нет")
                    }
                    TextButton(onConfirmRequest) {
                        Text(text = "Да")
                    }
                }
            }
        }
    }
}