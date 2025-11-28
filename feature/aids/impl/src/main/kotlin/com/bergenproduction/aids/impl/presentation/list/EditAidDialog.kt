package com.bergenproduction.aids.impl.presentation.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAidDialog(
    initialName: String,
    onDismissRequest: () -> Unit,
    onEditRequest: (String) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    var name by remember { mutableStateOf(initialName) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        ElevatedCard(
            modifier = Modifier.Companion.wrapContentSize(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.Companion.padding(24.dp)) {
                Icon(
                    Icons.Rounded.Edit, null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.Companion.CenterHorizontally)
                )
                Text(
                    text = "Изменить аптечку",
                    modifier = Modifier
                        .align(Alignment.Companion.CenterHorizontally)
                        .padding(top = 16.dp),
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Укажите название аптечки",
                    modifier = Modifier.Companion
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
                    modifier = Modifier.Companion.padding(bottom = 24.dp)
                )
                Row(modifier = Modifier.align(Alignment.Companion.End)) {
                    TextButton(onDismissRequest) {
                        Text(text = "Отмена")
                    }
                    TextButton(
                        onClick = { onEditRequest.invoke(name) },
                        enabled = initialName != name && name.isNotEmpty()
                    ) {
                        Text(text = "Ок")
                    }
                }
            }
        }
    }
}