package com.dyablonskyi.transpod.ui.screen.admin.driver

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

@Composable
fun CountDriverWithoutTransportDialog(
    num: Int,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "$num", fontSize = 34.sp, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun GetDriversByRouteIdDialog(
    routes: List<Route>,
    drivers: List<Driver>,
    onSelect: (Long) -> Unit,
    onDismissRequest: () -> Unit,
) {

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedRoutesNamesTextField(
                    routes = routes,
                    onSelect = onSelect,
                    modifier = Modifier.padding(10.dp)
                )
                if (drivers.isNotEmpty()) {
                    LazyColumn {
                        items(
                            items = drivers,
                            key = { driver -> driver.id }
                        ) { driver ->
                            DriverItem(id = driver.id, fullName = driver.fullName)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedRoutesNamesTextField(
    routes: List<Route>,
    onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRoute by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedRoute,
            placeholder = { Text(if (routes.isNotEmpty()) "Route" else "No routes available") },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
        )
        if (routes.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize(true)
            ) {
                routes.forEach { route ->
                    DropdownMenuItem(
                        text = { Text(route.name) },
                        onClick = {
                            selectedRoute = route.name
                            onSelect(route.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DriverItem(
    id: Long,
    fullName: String
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = id.toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .width(70.dp)
                    .fillMaxWidth()
            )
            Text(
                text = fullName,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Divider(Modifier.padding(horizontal = 10.dp))
    }
}

@Preview
@Composable
private fun DialogPreview() {
    TranspodTheme {
        CountDriverWithoutTransportDialog(num = 10) {

        }
    }
}

@Preview
@Composable
private fun Dialog2Preview() {

    val drivers = listOf(
        Driver(
            id = 332,
            fullName = "John Doe",
            phoneNumber = "123-456-7890",
            address = "123 Main St, Anytown, USA",
            routeId = 1,
            transportId = 1
        ),
        Driver(
            id = 1,
            fullName = "Jane Smith",
            phoneNumber = "987-654-3210",
            address = "456 Elm St, Othertown, USA",
            routeId = 2,
            transportId = 1
        ),
        Driver(
            id = 2,
            fullName = "Michael Johnson",
            phoneNumber = "555-555-5555",
            address = "789 Oak St, Anycity, USA",
            routeId = 3,
            transportId = null
        ),
        Driver(
            id = 3,
            fullName = "Emily Williams",
            phoneNumber = "222-333-4444",
            address = "321 Pine St, Someville, USA",
            routeId = null,
            transportId = 2
        ),
        Driver(
            id = 4222,
            fullName = "Chris Brown",
            phoneNumber = "777-888-9999",
            address = "555 Cedar St, Othertown, USA",
            routeId = 4,
            transportId = 2
        ),
        Driver(
            id = 4322,
            fullName = "Chris Brown",
            phoneNumber = "777-888-9999",
            address = "555 Cedar St, Othertown, USA",
            routeId = 4,
            transportId = 2
        )
    )
    TranspodTheme {
        GetDriversByRouteIdDialog(
            routes = emptyList(),
            drivers = drivers,
            onSelect = {},
            onDismissRequest = {}
        )
    }
}