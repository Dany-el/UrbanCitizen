package com.dyablonskyi.transpod.ui.screen.admin.driver

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.dyablonskyi.transpod.R
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import com.dyablonskyi.transpod.ui.LoadingScreen
import com.dyablonskyi.transpod.ui.UIState
import com.dyablonskyi.transpod.ui.navigateSingleToTop
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportViewModel
import com.dyablonskyi.transpod.ui.theme.TranspodTheme
import com.dyablonskyi.transpod.ui.util.DriverQuery
import com.dyablonskyi.transpod.ui.util.InsertScreen
import com.dyablonskyi.transpod.ui.util.PhoneVisualTransformation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverListScreen(
    drivers: List<DriverWithRouteAndTransport>,
    onInsertButtonClick: () -> Unit,
    onValueChange: (DriverQuery) -> Unit
) {
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (drivers.isNotEmpty()) "Here is the list" else "The list is empty",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = { dropDownMenuExpanded = false }
                    ) {
                        DriverQuery.entries.forEach { query ->
                            DropdownMenuItem(
                                text = { Text(query.stringItem) },
                                onClick = {
                                    dropDownMenuExpanded = false
                                    onValueChange(query)
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onInsertButtonClick,
                Modifier
                    .padding(20.dp)
                    .size(74.dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            DriverList(drivers = drivers)
        }
    }
}

@Composable
fun DriverList(
    drivers: List<DriverWithRouteAndTransport>
) {
    LazyColumn {
        items(
            drivers,
            key = { driver -> driver.driver.id }
        ) {
            DriverItem(it)
        }
    }
}

@Composable
fun DriverItem(
    link: DriverWithRouteAndTransport
) {
    val (firstName, lastName) = link.driver.fullName.split(" ")

    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .shadow(3.dp, MaterialTheme.shapes.medium)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Text(text = firstName)
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(text = lastName, modifier = Modifier.padding(end = 10.dp))
                    }
                    Text(text = "+38${link.driver.phoneNumber}")
                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = link.driver.address,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Divider(Modifier.padding(end = 5.dp, top = 5.dp, bottom = 5.dp))

                    Text(
                        text = "Route: ${link.route?.name ?: "None"}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = "Transport #${link.transport?.number ?: "None"}")
                }
            }
            Text(text = link.driver.id.toString(), modifier = Modifier.padding(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverInsertScreen(
    routes: List<Route>,
    transports: List<Transport>,
    onInsertButtonClick: (Driver) -> Unit,
    showToastMessage: (String) -> Unit
) {
    val errorMessage = "Text field must be filled"

    // First name field
    var firstName by rememberSaveable { mutableStateOf("") }
    var isErrorNameField by rememberSaveable { mutableStateOf(false) }

    // Last name field
    var lastName by rememberSaveable { mutableStateOf("") }
    var isErrorLastNameField by rememberSaveable { mutableStateOf(false) }

    // Phone number field
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var isErrorPhoneNumberField by rememberSaveable { mutableStateOf(false) }
    var isNotEnoughDigits by rememberSaveable { mutableStateOf(false) }

    // Address field
    var address by rememberSaveable { mutableStateOf("") }
    var isErrorAddressField by rememberSaveable { mutableStateOf(false) }

    // Route field
    var routeName by rememberSaveable { mutableStateOf("") }
    var isRoutesExpanded by remember { mutableStateOf(false) }
    var routeIndex by rememberSaveable { mutableIntStateOf(-1) }

    // Transport field
    var transportNumber by rememberSaveable { mutableStateOf("") }
    var isTransportsExpanded by remember { mutableStateOf(false) }
    var transportIndex by rememberSaveable { mutableIntStateOf(-1) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { s ->
                    firstName = s.filter { !it.isWhitespace() }
                    isErrorNameField = firstName.isBlank()
                },
                label = { Text(text = "Name") },
                isError = isErrorNameField,
                supportingText = {
                    if (isErrorNameField)
                        Text(errorMessage)
                },
                trailingIcon = {
                    if (isErrorNameField)
                        Icon(
                            painterResource(R.drawable.ic_outline_error),
                            "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                modifier = Modifier.padding(10.dp)
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { s ->
                    lastName = s.filter { !it.isWhitespace() }
                    isErrorLastNameField = lastName.isBlank()
                },
                label = { Text(text = "Surname") },
                isError = isErrorLastNameField,
                supportingText = {
                    if (isErrorLastNameField)
                        Text(errorMessage)
                },
                trailingIcon = {
                    if (isErrorLastNameField)
                        Icon(
                            painterResource(R.drawable.ic_outline_error),
                            "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                modifier = Modifier.padding(10.dp)
            )
            PhoneNumberOutlinedField(
                phone = phoneNumber,
                onPhoneChanged = {
                    phoneNumber = it
                    isErrorPhoneNumberField = phoneNumber.isBlank()
                    isNotEnoughDigits = phoneNumber.length < 10
                },
                isError = isErrorPhoneNumberField || isNotEnoughDigits,
                supportingText = {
                    if (isErrorPhoneNumberField)
                        Text(errorMessage)
                    else if (isNotEnoughDigits)
                        Text("Not enough digits in phone number(${phoneNumber.length}/10)")
                },
                trailingIcon = {
                    if (isErrorPhoneNumberField || isNotEnoughDigits)
                        Icon(
                            painterResource(R.drawable.ic_outline_error),
                            "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                modifier = Modifier.padding(10.dp)
            )
            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    isErrorAddressField = address.isBlank()
                },
                label = { Text(text = "Address") },
                isError = isErrorAddressField,
                supportingText = {
                    if (isErrorAddressField)
                        Text(errorMessage)
                },
                trailingIcon = {
                    if (isErrorAddressField)
                        Icon(
                            painterResource(R.drawable.ic_outline_error),
                            "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                },
                modifier = Modifier.padding(10.dp)
            )
            ExposedDropdownMenuBox(
                expanded = isRoutesExpanded,
                onExpandedChange = { isRoutesExpanded = !isRoutesExpanded },
                modifier = Modifier
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = routeName,
                    placeholder = { Text(if (routes.isNotEmpty()) "Route" else "No routes available") },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRoutesExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                if (routes.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = isRoutesExpanded,
                        onDismissRequest = { isRoutesExpanded = false }
                    ) {
                        routes.forEachIndexed { index, route ->
                            DropdownMenuItem(
                                text = { Text(route.name) },
                                onClick = {
                                    routeName = route.name
                                    routeIndex = index
                                    isRoutesExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = isTransportsExpanded,
                onExpandedChange = { isTransportsExpanded = !isTransportsExpanded },
                modifier = Modifier
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = transportNumber,
                    placeholder = { Text(if (transports.isNotEmpty()) "Transport" else "No transports available") },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTransportsExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                if (transports.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = isTransportsExpanded,
                        onDismissRequest = { isTransportsExpanded = false }
                    ) {
                        transports.forEachIndexed { index, transport ->
                            DropdownMenuItem(
                                text = { Text("[%04d]".format(transport.number)) },
                                onClick = {
                                    transportNumber = "[%04d]".format(transport.number)
                                    transportIndex = index
                                    isTransportsExpanded = false
                                }
                            )
                        }
                    }
                }
            }

        }
        Button(
            onClick = {
                isErrorNameField = firstName.isBlank()
                isErrorLastNameField = lastName.isBlank()
                isErrorPhoneNumberField = phoneNumber.isBlank()
                isErrorAddressField = address.isBlank()
                isNotEnoughDigits = phoneNumber.length < 10

                val errors = listOf(
                    isErrorNameField,
                    isErrorLastNameField,
                    isErrorPhoneNumberField,
                    isErrorAddressField,
                    isNotEnoughDigits
                )
                if (errors.any { it }) {
                    showToastMessage("There are fields with errors!")
                } else {
                    onInsertButtonClick(
                        Driver(
                            fullName = "$firstName $lastName",
                            phoneNumber = phoneNumber,
                            address = address,
                            routeId = if (routeIndex >= 0) routes[routeIndex].id else null,
                            transportId = if (transportIndex >= 0) transports[transportIndex].number.toLong() else null
                        )
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 25.dp)
                .width(130.dp)
                .height(50.dp)
        ) {
            Text(text = "Insert")
        }
    }
}

@Composable
fun PhoneNumberOutlinedField(
    phone: String,
    modifier: Modifier = Modifier,
    mask: String = "000 000 0000",
    maskNumber: Char = '0',
    prefix: String = "+38",
    onPhoneChanged: (String) -> Unit,
    isError: Boolean,
    supportingText: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit
) {
    OutlinedTextField(
        value = phone,
        onValueChange = { it ->
            val numbers = it.filter { it.isDigit() }
            onPhoneChanged(numbers.take(mask.count { it == maskNumber }))
        },
        label = {
            Text(text = "Phone number")
        },
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = supportingText,
        prefix = { Text(prefix) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        modifier = modifier
    )
}

@Composable
fun DriverScreenWithDialogs(
    driverViewModel: DriverViewModel,
    uiState: UIState,
    navController: NavHostController
) {
    var showDialog1 by remember {
        mutableStateOf(false)
    }

    var showDialog2 by remember {
        mutableStateOf(false)
    }

    DriverListScreen(
        drivers = uiState.drivers,
        onInsertButtonClick = {
            navController.navigateSingleToTop(InsertScreen.DriverInsert.route)
        },
        onValueChange = { query ->
            when (query) {
                DriverQuery.COUNT_DRIVER_WITHOUT_TRANSPORT -> showDialog1 = true
                DriverQuery.GET_DRIVERS_BY_ROUTE -> showDialog2 = true
            }
        }
    )
    if (showDialog1) {
        var num: Int by remember { mutableIntStateOf(-1) }
        LaunchedEffect(num) {
            num = driverViewModel.countDriversWithoutTransport()
        }
        if (num < 0) {
            Dialog(onDismissRequest = { }) {
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .width(100.dp)
                ) {
                    LinearProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
        } else {
            CountDriverWithoutTransportDialog(
                num = num,
                onDismissRequest = { showDialog1 = false }
            )
        }
    }

    if (showDialog2) {
        var list: List<Driver> by remember { mutableStateOf(emptyList()) }
        val coroutineScope = rememberCoroutineScope()

        GetDriversByRouteIdDialog(
            routes = uiState.routes,
            drivers = list,
            onSelect = { id ->
                coroutineScope.launch {
                    list = driverViewModel.getDriversByRouteId(id)
                }
            },
            onDismissRequest = {
                showDialog2 = false
            }
        )
    }
}

@Composable
fun DriverInsertScreenWithLoading(
    driverViewModel: DriverViewModel,
    transportViewModel: TransportViewModel,
    uiState: UIState,
    navController: NavHostController,
    context: Context = LocalContext.current
) {
    LaunchedEffect(Unit) {
        transportViewModel.loadAvailableTransports()
    }
    if (uiState.isTransportLoading) {
        LoadingScreen()
    } else {
        DriverInsertScreen(
            routes = uiState.routes,
            transports = uiState.availableTransports,
            showToastMessage = { errorMsg ->
                Toast.makeText(
                    context,
                    errorMsg,
                    Toast.LENGTH_SHORT
                ).show()
            },
            onInsertButtonClick = {
                driverViewModel.insertDriver(it)
                navController.popBackStack()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DriverItemPreview() {
    TranspodTheme {
        DriverItem(
            DriverWithRouteAndTransport(
                driver = Driver(
                    fullName = "Name Surname",
                    phoneNumber = "0123456789",
                    address = "st. Somewhere 12/2",
                    routeId = 1,
                    transportId = 1
                ),
                route = Route(
                    name = "South-West",
                    routeStart = "st. Start",
                    routeEnd = "st. End",
                ),
                transport = Transport(
                    number = 1234,
                    type = TransportType.BUS
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DriverInsertScreenPreview() {
    TranspodTheme {
        DriverInsertScreen(
            routes = listOf(
                Route(name = "Route 1", routeStart = "Start", routeEnd = "End"),
                Route(name = "Route 2", routeStart = "Start", routeEnd = "End"),
                Route(name = "Route 3", routeStart = "Start", routeEnd = "End"),
                Route(name = "Route 4", routeStart = "Start", routeEnd = "End"),
            ),
            transports = listOf(
                Transport(
                    number = 1234,
                    type = TransportType.BUS
                ),
                Transport(
                    number = 1235,
                    type = TransportType.BUS
                ),
                Transport(
                    number = 1233,
                    type = TransportType.BUS
                )
            ),
            onInsertButtonClick = {},
            showToastMessage = {}
        )
    }
}

@Preview
@Composable
private fun DriverListScreenPreview() {
    TranspodTheme {
        DriverListScreen(drivers = emptyList(), onInsertButtonClick = {}, onValueChange = {})
    }
}