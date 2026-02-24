@file:OptIn(ExperimentalMaterial3Api::class)

package com.bergenproduction.aids.impl.presentation.list.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bergenproduction.aids.impl.R

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
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = title,
                    modifier = Modifier,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = body,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismissRequest) {
                        Text(text = stringResource(R.string.no))
                    }
                    TextButton(onConfirmRequest) {
                        Text(text = stringResource(R.string.yes))
                    }
                }
            }
        }
    }
}