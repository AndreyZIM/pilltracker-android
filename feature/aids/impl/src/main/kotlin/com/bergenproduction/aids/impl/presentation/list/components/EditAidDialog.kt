package com.bergenproduction.aids.impl.presentation.list.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bergenproduction.aids.impl.R

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
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Icon(
                    Icons.Rounded.Edit, null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(R.string.edit_aid),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.set_aid_name),
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
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismissRequest) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = { onEditRequest.invoke(name) },
                        enabled = initialName != name && name.isNotEmpty()
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}