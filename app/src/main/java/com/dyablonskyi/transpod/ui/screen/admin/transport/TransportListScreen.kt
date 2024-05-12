package com.dyablonskyi.transpod.ui.screen.admin.transport

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dyablonskyi.transpod.R
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import com.dyablonskyi.transpod.ui.screen.admin.route.InsertRouteDialog
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

@Composable
fun TransportListScreen(
    transports: List<Transport>,
    onInsertButtonClick: (Transport) -> Unit,
    showToastMessage: (String) -> Unit,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            Text(
                text = if (transports.isNotEmpty()) "Here is the list" else "Looks like the list is empty",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.size(30.dp))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                Modifier
                    .padding(20.dp)
                    .size(74.dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TransportList(
                transports
            )
        }
        AnimatedVisibility(showDialog) {
            InsertTransportDialog(
                onInsertButtonClick = { transport ->
                    onInsertButtonClick(transport)
                    showDialog = false
                },
                showToastMessage = showToastMessage,
                onDismissRequest = { showDialog = false }
            )
        }
    }
}

@Composable
fun TransportList(
    transports: List<Transport>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(
            transports,
            key = { transport -> transport.id }
        ) {
            TransportItem(number = it.number, type = it.type)
        }
    }
}

@Composable
fun TransportItem(
    number: Int,
    type: TransportType
) {
    val image = when (type) {
        TransportType.BUS -> painterResource(id = R.drawable.ic_bus)
        TransportType.TRAM -> painterResource(id = R.drawable.ic_tram)
        TransportType.TROLLEYBUS -> painterResource(id = R.drawable.ic_trolley)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(15.dp)
            .shadow(3.dp, MaterialTheme.shapes.medium)
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Image(
                    painter = image,
                    contentDescription = "Description",
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(48.dp)
                )
                Text(
                    text = "[$number]",
                    textAlign = TextAlign.Center,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTransportDialog(
    onInsertButtonClick: (Transport) -> Unit,
    showToastMessage: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val errorMessage = "Text field must be filled"

    var transportNumber by rememberSaveable { mutableStateOf("") }
    var isErrorNumberField by rememberSaveable { mutableStateOf(false) }

    val types = listOf("Bus", "Tram", "Trolley")
    var transportType by rememberSaveable { mutableStateOf(types.first()) }
    var isTypesExpanded by remember { mutableStateOf(false) }
    var transportTypeIndex by rememberSaveable { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Transport",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    OutlinedTextField(
                        value = transportNumber,
                        onValueChange = {
                            transportNumber = it
                            isErrorNumberField = transportNumber.isBlank()
                        },
                        label = { Text(text = "Number") },
                        isError = isErrorNumberField,
                        supportingText = {
                            if (isErrorNumberField)
                                Text(errorMessage)
                        },
                        trailingIcon = {
                            if (isErrorNumberField)
                                Icon(
                                    painterResource(R.drawable.ic_outline_error),
                                    "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(20.dp)
                    )
                    ExposedDropdownMenuBox(
                        expanded = isTypesExpanded,
                        onExpandedChange = { isTypesExpanded = !isTypesExpanded },
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = transportType,
                            placeholder = { Text(transportType) },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTypesExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = isTypesExpanded,
                            onDismissRequest = { isTypesExpanded = false }
                        ) {
                            types.forEachIndexed { index, type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        transportType = type
                                        transportTypeIndex = index
                                        isTypesExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                val errorMsg = stringResource(R.string.toast_error_msg)

                TextButton(
                    onClick = {
                        isErrorNumberField = transportNumber.isBlank()

                        if (isErrorNumberField) {
                            showToastMessage(errorMsg)
                        } else {
                            onInsertButtonClick(
                                Transport(
                                    number = transportNumber.toInt(),
                                    type = TransportType.entries[transportTypeIndex]
                                )
                            )
                        }

                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "Insert",
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
private fun TransportItemPreview() {
    TranspodTheme {
        TransportItem(
            number = 1234,
            type = TransportType.TROLLEYBUS
        )
    }
}

@Preview
@Composable
private fun InsertTransportDialogPreview() {
    TranspodTheme {
        InsertTransportDialog(
            onInsertButtonClick = {},
            showToastMessage = {},
            onDismissRequest = {})
    }
}