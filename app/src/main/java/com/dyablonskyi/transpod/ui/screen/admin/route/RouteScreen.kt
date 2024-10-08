package com.dyablonskyi.transpod.ui.screen.admin.route

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dyablonskyi.transpod.R
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.RouteDriverCount
import com.dyablonskyi.transpod.ui.UIState
import com.dyablonskyi.transpod.ui.theme.TranspodTheme
import com.dyablonskyi.transpod.ui.util.RouteQuery
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListScreen(
    routes: List<Route>,
    onInsertButtonClick: (Route) -> Unit,
    showToastMessage: (String) -> Unit,
    onValueChange: (RouteQuery) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (routes.isNotEmpty()) "Here is the list" else "The list is empty",
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
                        RouteQuery.entries.forEach { query ->
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
            RouteList(routes)
        }
        AnimatedVisibility(showDialog) {
            InsertRouteDialog(
                onInsertButtonClick = { route ->
                    onInsertButtonClick(route)
                    showDialog = false
                },
                showToastMessage = showToastMessage,
                onDismissRequest = { showDialog = false }
            )
        }
    }
}

@Composable
fun RouteList(
    routes: List<Route>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(
            routes,
            key = { route -> route.id }
        ) {
            RouteItem(name = it.name, start = it.routeStart, end = it.routeEnd)
        }
    }
}

@Composable
fun RouteItem(
    name: String,
    start: String,
    end: String
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(5.dp)
            .shadow(3.dp, MaterialTheme.shapes.small)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "\uD83D\uDE80 $start",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "\uD83C\uDFC1 $end",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun InsertRouteDialog(
    onInsertButtonClick: (Route) -> Unit,
    showToastMessage: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val errorMessage = "Text field must be filled"

    var routeName by rememberSaveable { mutableStateOf("") }
    var isErrorNameField by rememberSaveable { mutableStateOf(false) }

    var routeStart by rememberSaveable { mutableStateOf("") }
    var isErrorStartField by rememberSaveable { mutableStateOf(false) }

    var routeEnd by rememberSaveable { mutableStateOf("") }
    var isErrorEndField by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Route",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    OutlinedTextField(
                        value = routeName,
                        onValueChange = {
                            routeName = it
                            isErrorNameField = routeName.isBlank()
                        },
                        label = { Text(text = "Code name") },
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
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                    OutlinedTextField(
                        value = routeStart,
                        onValueChange = {
                            routeStart = it
                            isErrorStartField = routeStart.isBlank()
                        },
                        label = { Text(text = "Start st.") },
                        isError = isErrorStartField,
                        supportingText = {
                            if (isErrorStartField)
                                Text(errorMessage)
                        },
                        trailingIcon = {
                            if (isErrorStartField)
                                Icon(
                                    painterResource(R.drawable.ic_outline_error),
                                    "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                    OutlinedTextField(
                        value = routeEnd,
                        onValueChange = {
                            routeEnd = it
                            isErrorEndField = routeEnd.isBlank()
                        },
                        label = { Text(text = "End st.") },
                        isError = isErrorEndField,
                        supportingText = {
                            if (isErrorEndField)
                                Text(errorMessage)
                        },
                        trailingIcon = {
                            if (isErrorEndField)
                                Icon(
                                    painterResource(R.drawable.ic_outline_error),
                                    "Error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                val errorMsg = stringResource(R.string.toast_error_msg)

                TextButton(
                    onClick = {
                        isErrorNameField = routeName.isBlank()
                        isErrorStartField = routeStart.isBlank()
                        isErrorEndField = routeEnd.isBlank()

                        val errors = listOf(
                            isErrorNameField,
                            isErrorStartField,
                            isErrorEndField
                        )

                        if (errors.any { it }) {
                            showToastMessage(errorMsg)
                        } else {
                            onInsertButtonClick(
                                Route(
                                    name = routeName,
                                    routeStart = routeStart,
                                    routeEnd = routeEnd
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)

                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add_button",
                        modifier = Modifier
                            .size(34.dp)
                    )
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

@Composable
fun RouteScreenWithDialog(
    routeViewModel: RouteViewModel,
    uiState: UIState,
    context: Context = LocalContext.current
) {
    var showDialog1 by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    var showDialog4 by remember { mutableStateOf(false) }

    RouteListScreen(
        routes = uiState.routes,
        onInsertButtonClick = {
            routeViewModel.insertRoute(it)
        },
        showToastMessage = {
            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()
        },
        onValueChange = { query ->
            when (query) {
                RouteQuery.GET_ROUTES_BY_NAME ->
                    showDialog1 = true

                RouteQuery.COUNT_DRIVERS_PER_INDIVIDUAL_ROUTE ->
                    showDialog2 = true

                RouteQuery.COUNT_TOTAL_DRIVERS ->
                    showDialog3 = true

                RouteQuery.GET_ROUTES_WITH_TRANSPORT_TYPE_OF ->
                    showDialog4 = true

                RouteQuery.RETURN -> routeViewModel.loadRoutes()
            }
        }
    )
    Box(Modifier.fillMaxSize()) {
        when {
            showDialog1 -> {
                val coroutineScope = rememberCoroutineScope()
                SearchDialog(
                    onSearchButtonClick = { searchResult ->
                        if (searchResult.isNotBlank() || searchResult.isNotEmpty()) {
                            coroutineScope.launch {
                                routeViewModel.filterRoutesByName(searchResult)
                            }
                        }
                    },
                    onDismissRequest = { showDialog1 = false }
                )
            }

            showDialog2 -> {
                var list: List<RouteDriverCount> by remember {
                    mutableStateOf(
                        emptyList()
                    )
                }

                LaunchedEffect(Unit) {
                    list = routeViewModel.countDriversPerIndividualRoute()
                }

                RouteDriverCountDialog(
                    list = list,
                    onDismissRequest = { showDialog2 = false }
                )
            }

            showDialog3 -> {
                var list: List<RouteDriverCount> by remember {
                    mutableStateOf(
                        emptyList()
                    )
                }

                LaunchedEffect(Unit) {
                    list = routeViewModel.countTotalDriversPerRoute()
                }

                RouteDriverCountDialog(
                    list = list,
                    onDismissRequest = { showDialog3 = false }
                )
            }

            showDialog4 -> {
                val coroutineScope = rememberCoroutineScope()

                GetRoutesByTransportTypeDialog(
                    onSelect = { type ->
                        coroutineScope.launch {
                            routeViewModel.filterRoutesWithTransportTypeOf(type)
                        }
                    },
                    onDismissRequest = { showDialog4 = false }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RouteItemPrev() {
    TranspodTheme {
        RouteItem(name = "South-West", start = "st. Start", end = "st. End")
    }
}

@Preview
@Composable
private fun InsertRouteDialogPreview() {
    TranspodTheme {
        InsertRouteDialog(
            {},
            {},
            {}
        )
    }
}