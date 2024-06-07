package com.dyablonskyi.transpod.ui.screen.admin.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.dyablonskyi.transpod.data.local.db.entity.RouteDriverCount
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import com.dyablonskyi.transpod.ui.theme.TranspodTheme


@Composable
fun SearchDialog(
    onSearchButtonClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var search by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .height(200.dp)
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text("Route name") },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                TextButton(
                    onClick = {
                        onSearchButtonClick(search)
                        onDismissRequest()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(text = "Search", fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun RouteDriverCountDialog(
    list: List<RouteDriverCount>,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .height(400.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Name")
                        Text("Count")
                    }
                    LazyColumn {
                        items(list) { routeAndCount ->
                            RouteDriverCountItem(routeAndCount.routeName, routeAndCount.driverCount)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RouteDriverCountItem(
    name: String,
    count: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = name)
        Text("$count")
    }
}

@Composable
fun GetRoutesByTransportTypeDialog(
    onSelect: (TransportType) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var selectedType by remember {
        mutableStateOf(TransportType.BUS)
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTransportTypesTextField(
                    onSelect = { type ->
                        selectedType = type
                    },
                    modifier = Modifier.padding(10.dp)
                )
                TextButton(onClick = {
                    onSelect(selectedType)
                    onDismissRequest()
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(text = "Search", fontSize = 24.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTransportTypesTextField(
    onSelect: (TransportType) -> Unit,
    modifier: Modifier = Modifier
) {

    val types = listOf("Bus", "Tram", "Trolley")
    var selectedType by rememberSaveable { mutableStateOf(types.first()) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedType,
            placeholder = { Text(selectedType) },
            label = { Text("Type") },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEachIndexed { index, type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        selectedType = type
                        expanded = false
                        onSelect(TransportType.entries[index])
                    }
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
annotation class PreviewWithBackground

@PreviewWithBackground
@Composable
private fun SearchDialogPreview() {
    TranspodTheme {
        SearchDialog(onSearchButtonClick = {}) {

        }
    }
}

@PreviewWithBackground
@Composable
private fun CountDriverPerIndividualRouteDialogPreview() {
    val routeDriverCounts = listOf(
        RouteDriverCount(
            routeId = 1,
            routeName = "Route A",
            driverCount = 5
        ),
        RouteDriverCount(
            routeId = 2,
            routeName = "Route B",
            driverCount = 3
        ),
        RouteDriverCount(
            routeId = 3,
            routeName = "Route C",
            driverCount = 7
        ),
        RouteDriverCount(
            routeId = 4,
            routeName = "Route D",
            driverCount = 2
        ),
        RouteDriverCount(
            routeId = 5,
            routeName = "Route E",
            driverCount = 4
        )
    )

    TranspodTheme {
        RouteDriverCountDialog(list = routeDriverCounts) {

        }
    }
}

@PreviewWithBackground
@Composable
private fun RouteDriverCountItemPreview() {
    TranspodTheme {
        RouteDriverCountItem(name = "Some route name", count = 1234)
    }
}