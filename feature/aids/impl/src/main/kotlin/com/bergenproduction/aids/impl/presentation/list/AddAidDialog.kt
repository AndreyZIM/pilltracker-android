@file:OptIn(ExperimentalMaterial3Api::class)

package com.bergenproduction.aids.impl.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddAidDialog(
    onDismissRequest: () -> Unit,
    onAddRequest: (String) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    var name by remember { mutableStateOf("") }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        ElevatedCard(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Icon(
                    Icons.Default.AddCircle, null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Добавить аптечку",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Укажите название аптечки",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismissRequest) {
                        Text(text = "Отмена")
                    }
                    TextButton({ onAddRequest.invoke(name) }) {
                        Text(text = "Ок")
                    }
                }
            }
        }
    }
}