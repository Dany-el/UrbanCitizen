package com.dyablonskyi.transpod.ui.screen.user.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dyablonskyi.transpod.data.local.db.entity.Duration
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyTicketDialog(
    onBuyButtonClick: (Duration) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var price by rememberSaveable {
        mutableDoubleStateOf(Duration.HOUR.price)
    }

    var selectedDuration by rememberSaveable {
        mutableStateOf(Duration.HOUR)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val durations = listOf(
        "Hour", "Day", "Month", "6 Months", "Year"
    )

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(345.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Ticket",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier.clickable {
                            expanded = !expanded
                        }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = durations[Duration.entries.indexOf(selectedDuration)],
                            onValueChange = {},
                            label = { Text("Duration") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            durations.forEachIndexed { index, duration ->
                                DropdownMenuItem(
                                    text = { Text(duration) },
                                    onClick = {
                                        selectedDuration = Duration.entries[index]
                                        price = Duration.entries[index].price
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                    Spacer(Modifier.size(20.dp))
                    Text(
                        text = "Price $price UAH",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }

                TextButton(
                    onClick = { onBuyButtonClick(selectedDuration) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Icon(imageVector = Icons.Rounded.ShoppingCart, contentDescription = "Cart")
                    Text(
                        text = "Buy",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BuyTicketDialogPreview() {
    TranspodTheme {
        BuyTicketDialog({}, {})
    }
}