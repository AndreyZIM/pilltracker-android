package com.bergenproduction.pilltracker.core.ui.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bergenproduction.pilltracker.core.ui.R
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.PillTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

object PreparationCardDefaults {
    val typeStrings = listOf(
        R.string.preparation_antiviral,
        R.string.preparation_antibiotic,
        R.string.preparation_sedative,
        R.string.preparation_painkiller,
        R.string.preparation_antiinflammatory,
        R.string.preparation_laxative,
        R.string.preparation_mucolytic,
        R.string.preparation_antihistamine,
        R.string.preparation_hypnotic,
        R.string.preparation_dietarysupplement,
        R.string.preparation_vitamin,
        R.string.preparation_heatlower,
        R.string.preparation_bloodboiler,
        R.string.preparation_kok,
    )

    const val DATE_PATTERN = "dd.MM.yyyy"
}

@Composable
fun PreparationCard(
    preparationId: Int,
    aidId: Int,
    name: String,
    dosage: Int,
    expiration: Date,
    recommendations: String,
    past: Boolean,
    days: Int,
    months: Int,
    years: Int,
    type: List<Int>,
    modifier: Modifier = Modifier,
    onDeletePress: (Int) -> Unit = {},
    onClick: (Int) -> Unit = {}
) {
    val cardBgColor =
        if (past) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.surfaceContainerHighest
    val textColor =
        if (past) MaterialTheme.colorScheme.onErrorContainer
        else MaterialTheme.colorScheme.onSurfaceVariant
    val otherColor =
        if (past) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.outlineVariant
    val textColor2 =
        if (past) MaterialTheme.colorScheme.onErrorContainer
        else MaterialTheme.colorScheme.outline

    val formatter = SimpleDateFormat(PreparationCardDefaults.DATE_PATTERN, Locale.getDefault())
    val formattedDate = formatter.format(expiration)

    Card(
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(true) { onClick.invoke(preparationId) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = stringResource(R.string.preparation_card_name, name, dosage),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1F)
                )
                IconButton(
                    onClick = { onDeletePress.invoke(preparationId) }
                ) {
                    Icon(painterResource(R.drawable.ic_delete), null, tint = textColor)
                }
            }
            Text(
                text = stringResource(R.string.preparation_card_expiration_text, formattedDate),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = textColor2,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            if (type.isNotEmpty())
                FlowRow(
                    horizontalGap = 8.dp,
                    verticalGap = 0.dp,
                    alignment = Alignment.Start,
                ) {
                    type.forEach {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(text = stringResource(PreparationCardDefaults.typeStrings[it]))
                            },
                            colors = AssistChipDefaults.assistChipColors(labelColor = textColor),
                            border = AssistChipDefaults.assistChipBorder(
                                true,
                                borderColor = otherColor
                            ),
                        )
                    }
                }
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                color = otherColor
            )

            val countDownText =
                "${years.absoluteValue} ${
                    pluralStringResource(
                        R.plurals.years_ago,
                        years,
                        years,
                    )
                } ${months.absoluteValue} ${
                    pluralStringResource(
                        R.plurals.months_ago,
                        months,
                        months,
                    )
                } ${days.absoluteValue} ${
                    pluralStringResource(
                        R.plurals.days_ago,
                        days,
                        days,
                    )
                }"

            Text(
                text = if (past) "- $countDownText" else countDownText,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                color = textColor,
            )
        }
    }
}

@Composable
private fun PreparationCardPreviewParameter(error: Boolean) {
    PillTrackerTheme {
        PreparationCard(
            preparationId = 0,
            aidId = 0,
            name = "Мукалтин",
            dosage = 200,
            expiration = Date(),
            recommendations = "Пропрпрпрпрро овпр ыоврп оывпр оыврпыовопрывопрыовп",
            past = error,
            days = 0,
            months = 0,
            years = 0,
            type = listOf(1, 2, 3, 4),
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Preview
@Composable
private fun PreparationCardPreview() {
    PreparationCardPreviewParameter(true)
}

@Preview
@Composable
private fun PreparationCardPreview2() {
    PreparationCardPreviewParameter(false)
}