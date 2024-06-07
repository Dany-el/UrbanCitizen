package com.dyablonskyi.transpod

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dyablonskyi.transpod.data.local.db.entity.Driver
import com.dyablonskyi.transpod.data.local.db.entity.RouteDriverCount
import com.dyablonskyi.transpod.data.local.db.entity.TicketBuilder
import com.dyablonskyi.transpod.data.local.db.entity.TransportType
import com.dyablonskyi.transpod.ui.screen.admin.AdminScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.CountDriverWithoutTransportDialog
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverInsertScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverListScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverViewModel
import com.dyablonskyi.transpod.ui.screen.admin.driver.GetDriversByRouteIdDialog
import com.dyablonskyi.transpod.ui.screen.admin.route.GetRoutesByTransportTypeDialog
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteDriverCountDialog
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteListScreen
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteViewModel
import com.dyablonskyi.transpod.ui.screen.admin.route.SearchDialog
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportListScreen
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportViewModel
import com.dyablonskyi.transpod.ui.screen.main.MainScreen
import com.dyablonskyi.transpod.ui.screen.user.UserScreen
import com.dyablonskyi.transpod.ui.screen.user.ticket.DateRangePicker
import com.dyablonskyi.transpod.ui.screen.user.ticket.TicketViewModel
import com.dyablonskyi.transpod.ui.theme.TranspodTheme
import com.dyablonskyi.transpod.ui.util.DriverQuery
import com.dyablonskyi.transpod.ui.util.InsertScreen
import com.dyablonskyi.transpod.ui.util.ListScreen
import com.dyablonskyi.transpod.ui.util.MainScreen
import com.dyablonskyi.transpod.ui.util.RouteQuery
import com.dyablonskyi.transpod.ui.util.TicketQuery
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@AndroidEntryPoint(ComponentActivity::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranspodApp()
        }
    }
}

@Composable
fun TranspodApp() {
    TranspodTheme {
        val navController = rememberNavController()
        Surface(
            tonalElevation = 8.dp
        ) {
            TranspodNavHost(navController = navController)
        }
    }
}

@Composable
fun TranspodNavHost(
    navController: NavHostController,
    context: Context = LocalContext.current,
    ticketViewModel: TicketViewModel = viewModel(),
    driverViewModel: DriverViewModel = viewModel(),
    transportViewModel: TransportViewModel = viewModel(),
    routeViewModel: RouteViewModel = viewModel(),
) {

    // Lists
    val tickets by ticketViewModel.tickets.collectAsState()
    val routes by routeViewModel.routes.collectAsState()
    val drivers by driverViewModel.drivers.collectAsState()
    val transports by transportViewModel.transports.collectAsState()
    val availableTransports by transportViewModel.availableTransports.collectAsState()

    // Loading states
    val isTicketsLoading by ticketViewModel.isLoading.collectAsState()
    val isRoutesLoading by routeViewModel.isLoading.collectAsState()
    val isDriversLoading by driverViewModel.isLoading.collectAsState()
    val isTransportLoading by transportViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        ticketViewModel.loadTickets()
        driverViewModel.loadDrivers()
        transportViewModel.loadTransports()
        routeViewModel.loadRoutes()
    }

    if (
        isTicketsLoading ||
        isRoutesLoading ||
        isDriversLoading ||
        isTransportLoading
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = MainScreen.Main.route
        ) {
            composable(route = MainScreen.Main.route) {
                MainScreen(
                    onUserButtonClick = { navController.navigateSingleToTop("user") },
                    onAdminButtonClick = { navController.navigateSingleToTop("admin") }
                )
            }
            composable(route = MainScreen.User.route) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    if (isTicketsLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        var showDialog by remember { mutableStateOf(false) }
                        val coroutineScope = rememberCoroutineScope()
                        UserScreen(
                            tickets = tickets,
                            onBuyButtonClick = { duration ->
                                ticketViewModel.insertTicket(TicketBuilder(duration, null).build())
                                Toast.makeText(
                                    context,
                                    "Have a safe trip",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onValueChange = { query ->
                                when (query) {
                                    TicketQuery.GET_TICKETS_BY_START_DATE_IN_DURATION ->
                                        showDialog = true

                                    TicketQuery.RETURN -> {
                                        ticketViewModel.loadTickets()
                                    }
                                }
                            }
                        )
                        if (showDialog) {
                            DateRangePicker(
                                onSaveClick = {
                                    val from = Instant
                                        .ofEpochMilli(it.first)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()
                                    val to =
                                        Instant
                                            .ofEpochMilli(it.last)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime()

                                    coroutineScope.launch {
                                        ticketViewModel.filterTicketsByStartDateInDuration(from, to)
                                        showDialog = false
                                    }
                                },
                                onDismissRequest = { showDialog = false }
                            )
                        }
                    }
                }
            }
            composable(route = MainScreen.Admin.route) {
                AdminScreen(
                    onDriverButtonClick = { navController.navigateSingleToTop("drivers") },
                    onTransportButtonClick = { navController.navigateSingleToTop("transports") },
                    onRouteButtonClick = { navController.navigateSingleToTop("routes") }
                )
            }
            composable(route = ListScreen.Routes.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (isRoutesLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        var showDialog1 by remember { mutableStateOf(false) }
                        var showDialog2 by remember { mutableStateOf(false) }
                        var showDialog3 by remember { mutableStateOf(false) }
                        var showDialog4 by remember { mutableStateOf(false) }

                        RouteListScreen(
                            routes = routes,
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
                                val scope = rememberCoroutineScope()

                                GetRoutesByTransportTypeDialog(
                                    onSelect = { type ->
                                        scope.launch {
                                            routeViewModel.filterRoutesWithTransportTypeOf(type)
                                        }
                                    },
                                    onDismissRequest = { showDialog4 = false }
                                )
                            }
                        }
                    }
                }
            }
            composable(route = ListScreen.Drivers.route) {
                if (isDriversLoading) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                } else {
                    var show1Dialog by remember {
                        mutableStateOf(false)
                    }

                    var show2Dialog by remember {
                        mutableStateOf(false)
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        DriverListScreen(
                            drivers = drivers,
                            onInsertButtonClick = {
                                navController.navigateSingleToTop(InsertScreen.DriverInsert.route)
                            },
                            onValueChange = { query ->
                                when (query) {
                                    DriverQuery.COUNT_DRIVER_WITHOUT_TRANSPORT -> show1Dialog = true
                                    DriverQuery.GET_DRIVERS_BY_ROUTE -> show2Dialog = true
                                }
                            }
                        )
                        if (show1Dialog) {
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
                                    onDismissRequest = { show1Dialog = false }
                                )
                            }
                        }

                        if (show2Dialog) {
                            var list: List<Driver> by remember { mutableStateOf(emptyList()) }
                            val coroutineScope = rememberCoroutineScope()

                            GetDriversByRouteIdDialog(
                                routes = routes,
                                drivers = list,
                                onSelect = { id ->
                                    coroutineScope.launch {
                                        list = driverViewModel.getDriversByRouteId(id)
                                    }
                                },
                                onDismissRequest = {
                                    show2Dialog = false
                                }
                            )
                        }
                    }
                }
            }
            composable(route = InsertScreen.DriverInsert.route) {
                LaunchedEffect(Unit) {
                    transportViewModel.loadAvailableTransports()
                }
                if (isTransportLoading) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                } else {
                    DriverInsertScreen(
                        routes = routes,
                        transports = availableTransports,
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
            composable(route = ListScreen.Transports.route) {
                if (isTransportLoading) {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                } else {
                    TransportListScreen(
                        transports = transports,
                        onInsertButtonClick = { transport ->
                            transportViewModel.insertTransport(transport)
                        }
                    )
                }
            }
        }
    }


}

fun NavHostController.navigateSingleToTop(route: String) =
    this.navigate(route) {
        restoreState = true
        launchSingleTop = true
    }