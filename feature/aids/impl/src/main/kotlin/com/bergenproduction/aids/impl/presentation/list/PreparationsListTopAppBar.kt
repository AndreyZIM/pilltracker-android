@file:OptIn(ExperimentalMaterial3Api::class)

package com.bergenproduction.aids.impl.presentation.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bergenproduction.aids.impl.R
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.ThemeIcons


internal object PreparationsListTopAppBarDefaults {
    val textFieldFontSize = 22.sp
    val textFieldFontHeight = 28.sp
    const val URL = "https://apteka.103.by/"
}

@Composable
internal fun PreparationsListTopAppBar(
    modifier: Modifier = Modifier,
    searchState: SearchState?,
    onSearchValueChange: (String) -> Unit,
    onSearchDisableClick: () -> Unit,
    onSearchEnableClick: () -> Unit,
    onQuestionsPressed: () -> Unit,
) {
    var showMoreDropDownMenu by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val uriHandler = LocalUriHandler.current
    AnimatedContent(
        targetState = searchState?.enabled ?: false,
        label = "search",
        modifier = modifier
    ) { searchEnabled ->
        if (searchEnabled) {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = searchState?.query ?: "",
                        onValueChange = onSearchValueChange,
                        textStyle = TextStyle.Default.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = PreparationsListTopAppBarDefaults.textFieldFontSize,
                            lineHeight = PreparationsListTopAppBarDefaults.textFieldFontHeight,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )
                },
                actions = {
                    IconButton(onClick = onSearchDisableClick) {
                        Icon(ThemeIcons.Close, null)
                    }
                },
            )
            LaunchedEffect(null) {
                focusRequester.requestFocus()
            }
        } else {
            TopAppBar(
                title = { Text(text = "PillTracker", fontWeight = FontWeight.Medium) },
                actions = {
                    IconButton(onClick = { uriHandler.openUri(PreparationsListTopAppBarDefaults.URL) }) {
                        Icon(ThemeIcons.Language, null)
                    }
                    IconButton(onClick = onSearchEnableClick) {
                        Icon(ThemeIcons.Search, null)
                    }
                    IconButton(onClick = { showMoreDropDownMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, null)
                    }
                    DropdownMenu(
                        expanded = showMoreDropDownMenu,
                        onDismissRequest = { showMoreDropDownMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Q&A") },
                            onClick = {
                                showMoreDropDownMenu = false
                                onQuestionsPressed.invoke()
                            },
                            leadingIcon = { Icon(Icons.Rounded.Edit, null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.user_manual)) },
                            onClick = { showMoreDropDownMenu = false },
                            enabled = false,
                            leadingIcon = {
                                Icon(ThemeIcons.Book, null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.compatibility_calculator)) },
                            onClick = { showMoreDropDownMenu = false },
                            enabled = false,
                            leadingIcon = {
                                Icon(ThemeIcons.Calculate, null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.support)) },
                            onClick = { showMoreDropDownMenu = false },
                            enabled = false,
                            leadingIcon = {
                                Icon(ThemeIcons.Support, null)
                            }
                        )
                    }
                },
            )
        }
    }

}