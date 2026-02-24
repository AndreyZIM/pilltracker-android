package com.bergenproduction.aids.impl.presentation.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bergenproduction.aids.impl.R
import com.bergenproduction.aids.impl.presentation.add.PreparationsDefaults
import com.bergenproduction.aids.impl.presentation.models.PreparationType
import com.bergenproduction.pilltracker.core.ui.designsystem.components.FlowRow
import com.bergenprodution.aids.api.navigation.PreparationInfoNavRoute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.collections.isNotEmpty

internal fun NavController.navigateToPreparationInfo(
    id: Int,
    aidId: Int,
    name: String,
    dosage: Int,
    expiration: Long,
    recommendations: String,
    past: Boolean,
    days: Int,
    months: Int,
    years: Int,
    type: List<PreparationType>,
    navOptions: NavOptions? = null
) {
    this.navigate(
        PreparationInfoNavRoute(
            id = id,
            aidId = aidId,
            name = name,
            dosage = dosage,
            expiration = expiration,
            recommendations = recommendations,
            past = past,
            days = days,
            months = months,
            years = years,
            type = Json.encodeToString(type)
        ),
        navOptions = navOptions
    )
}

@Composable
internal fun PreparationInfoRoute(
    onEditPress: (Int, Int, String, Int, Long, String, List<PreparationType>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    PreparationInfoScreen(
        onEditPress = onEditPress,
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreparationInfoScreen(
    viewModel: PreparationInfoViewModel = hiltViewModel<PreparationInfoViewModel>(),
    onEditPress: (Int, Int, String, Int, Long, String, List<PreparationType>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.name_and_dosage, viewModel.name, viewModel.dosage),
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onEditPress.invoke(
                            viewModel.id,
                            viewModel.aidId,
                            viewModel.name,
                            viewModel.dosage,
                            viewModel.expiration,
                            viewModel.recommendations,
                            viewModel.type
                        )
                    }) {
                        Icon(Icons.Default.Edit, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor
                )
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top),
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.expires_at, viewModel.expirationDate),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.expiration_duration, viewModel.years, viewModel.months, viewModel.days),
                fontSize = 22.sp,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)
            )
            if (viewModel.type.isNotEmpty())
                FlowRow(
                    horizontalGap = 8.dp,
                    verticalGap = 0.dp,
                    alignment = Alignment.Start,
                ) {
                    viewModel.type.forEach {
                        AssistChip(
                            onClick = {},
                            label = { Text(text = stringResource(PreparationsDefaults.typeStringMap[it]!!)) },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                true,
                                borderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                        )
                    }
                }
            if(viewModel.recommendations.isNotEmpty())
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.recomendations_for_taking),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Text(
                    text = viewModel.recommendations,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
