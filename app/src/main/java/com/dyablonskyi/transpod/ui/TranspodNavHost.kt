package com.dyablonskyi.transpod.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dyablonskyi.transpod.data.local.db.entity.DriverWithRouteAndTransport
import com.dyablonskyi.transpod.data.local.db.entity.Route
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.ui.screen.admin.AdminScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverInsertScreenWithLoading
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverScreenWithDialogs
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverViewModel
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteScreenWithDialog
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteViewModel
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportListScreen
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportViewModel
import com.dyablonskyi.transpod.ui.screen.main.MainScreen
import com.dyablonskyi.transpod.ui.screen.user.UserScreenWithDialog
import com.dyablonskyi.transpod.ui.screen.user.ticket.TicketViewModel
import com.dyablonskyi.transpod.ui.util.InsertScreen
import com.dyablonskyi.transpod.ui.util.ListScreen
import com.dyablonskyi.transpod.ui.util.MainScreen

@Composable
fun TranspodNavHost(
    navController: NavHostController,
    ticketViewModel: TicketViewModel = viewModel(),
    driverViewModel: DriverViewModel = viewModel(),
    transportViewModel: TransportViewModel = viewModel(),
    routeViewModel: RouteViewModel = viewModel(),
) {

    val uiState = collectAsUIState(
        ticketViewModel,
        driverViewModel,
        transportViewModel,
        routeViewModel
    )

    LaunchedEffect(Unit) {
        loadAllData(
            ticketViewModel,
            driverViewModel,
            transportViewModel,
            routeViewModel
        )
    }

    if (uiState.isLoading()) {
        LoadingScreen()
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
                UserScreenWithDialog(ticketViewModel = ticketViewModel, uiState = uiState)
            }
            composable(route = MainScreen.Admin.route) {
                AdminScreen(
                    onDriverButtonClick = { navController.navigateSingleToTop("drivers") },
                    onTransportButtonClick = { navController.navigateSingleToTop("transports") },
                    onRouteButtonClick = { navController.navigateSingleToTop("routes") }
                )
            }
            composable(route = ListScreen.Routes.route) {
                RouteScreenWithDialog(routeViewModel = routeViewModel, uiState = uiState)
            }
            composable(route = ListScreen.Drivers.route) {
                DriverScreenWithDialogs(
                    driverViewModel = driverViewModel,
                    uiState = uiState,
                    navController = navController
                )
            }
            composable(route = InsertScreen.DriverInsert.route) {
                DriverInsertScreenWithLoading(
                    driverViewModel = driverViewModel,
                    transportViewModel = transportViewModel,
                    uiState = uiState,
                    navController = navController
                )
            }
            composable(route = ListScreen.Transports.route) {
                TransportListScreen(
                    transports = uiState.transports,
                    onInsertButtonClick = { transport ->
                        transportViewModel.insertTransport(transport)
                    }
                )
            }
        }
    }
}


data class UIState(
    val tickets: List<Ticket>,
    val routes: List<Route>,
    val drivers: List<DriverWithRouteAndTransport>,
    val transports: List<Transport>,
    val availableTransports: List<Transport>,
    val isTicketsLoading: Boolean,
    val isRoutesLoading: Boolean,
    val isDriversLoading: Boolean,
    val isTransportLoading: Boolean
) {
    fun isLoading() = isTicketsLoading || isRoutesLoading || isDriversLoading || isTransportLoading
}

@Composable
fun collectAsUIState(
    ticketViewModel: TicketViewModel,
    driverViewModel: DriverViewModel,
    transportViewModel: TransportViewModel,
    routeViewModel: RouteViewModel
): UIState {
    return UIState(
        tickets = ticketViewModel.tickets.collectAsState().value,
        routes = routeViewModel.routes.collectAsState().value,
        drivers = driverViewModel.drivers.collectAsState().value,
        transports = transportViewModel.transports.collectAsState().value,
        availableTransports = transportViewModel.availableTransports.collectAsState().value,
        isTicketsLoading = ticketViewModel.isLoading.collectAsState().value,
        isRoutesLoading = routeViewModel.isLoading.collectAsState().value,
        isDriversLoading = driverViewModel.isLoading.collectAsState().value,
        isTransportLoading = transportViewModel.isLoading.collectAsState().value
    )
}

fun loadAllData(
    ticketViewModel: TicketViewModel,
    driverViewModel: DriverViewModel,
    transportViewModel: TransportViewModel,
    routeViewModel: RouteViewModel
) {
    ticketViewModel.loadTickets()
    driverViewModel.loadDrivers()
    transportViewModel.loadTransports()
    routeViewModel.loadRoutes()
}

@Composable
fun LoadingScreen() {
    Box(
        Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

fun NavHostController.navigateSingleToTop(route: String) =
    this.navigate(route) {
        restoreState = true
        launchSingleTop = true
    }