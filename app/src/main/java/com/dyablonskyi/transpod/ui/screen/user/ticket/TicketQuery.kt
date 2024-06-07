package com.dyablonskyi.transpod.ui.screen.user.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePicker(
    onSaveClick: (LongRange) -> Unit,
    onDismissRequest: () -> Unit
) {

    val state = rememberDateRangePickerState()
    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onDismissRequest) {
                    Icon(Icons.Filled.Close, contentDescription = "Close button")
                }
                TextButton(
                    onClick = {
                        val range =
                            state.selectedStartDateMillis!!..state.selectedEndDateMillis!!
                        onSaveClick(range)
                    },
                    enabled = state.selectedEndDateMillis != null
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            DateRangePicker(state = state, modifier = Modifier.weight(1f))
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun DatePickerPreview() {
    DateRangePicker(
        {},
        {}
    )
}