package com.dyablonskyi.transpod

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dyablonskyi.transpod.data.local.db.entity.TicketBuilder
import com.dyablonskyi.transpod.data.local.db.entity.Transport
import com.dyablonskyi.transpod.ui.screen.admin.AdminScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverInsertScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverListScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverViewModel
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteListScreen
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteViewModel
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportListScreen
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportViewModel
import com.dyablonskyi.transpod.ui.screen.main.MainScreen
import com.dyablonskyi.transpod.ui.screen.user.UserScreen
import com.dyablonskyi.transpod.ui.screen.user.ticket.TicketViewModel
import com.dyablonskyi.transpod.ui.theme.TranspodTheme
import dagger.hilt.android.AndroidEntryPoint

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

        TranspodNavHost(navController = navController)
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
    val tickets by ticketViewModel.tickets.collectAsState()
    val routes by routeViewModel.routes.collectAsState()
    val drivers by driverViewModel.drivers.collectAsState()
    val transports by transportViewModel.transports.collectAsState()
    var availableTransports: List<Transport> = emptyList()

    // Loading states
    val isTicketsLoading by ticketViewModel.isLoading.collectAsState()
    val isRoutesLoading by routeViewModel.isLoading.collectAsState()
    val isDriversLoading by driverViewModel.isLoading.collectAsState()
    val isTransportLoading by transportViewModel.isLoading.collectAsState()
    val isAvailableTransportListLoading by transportViewModel.isLoading.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable(route = "main_screen") {
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
                MainScreen(
                    onUserButtonClick = { navController.navigateSingleToTop("user") },
                    onAdminButtonClick = { navController.navigateSingleToTop("admin") }
                )
            }
        }

        composable(route = "user") {
            if (isTicketsLoading) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                UserScreen(
                    tickets = tickets,
                    onBuyButtonClick = { duration ->
                        ticketViewModel.insertTicket(TicketBuilder(duration, null).build())
                        Toast.makeText(
                            context,
                            "Have a safe trip",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }

        composable(route = "admin") {
            AdminScreen(
                onDriverButtonClick = { navController.navigateSingleToTop("drivers") },
                onTransportButtonClick = { navController.navigateSingleToTop("transports") },
                onRouteButtonClick = { navController.navigateSingleToTop("routes") }
            )
        }

        composable(route = "routes") {
            if (isRoutesLoading) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
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
                    }
                )
            }
        }

        composable(route = "drivers") {
            if (isDriversLoading) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                DriverListScreen(drivers = drivers) {
                    navController.navigateSingleToTop("add_driver")
                }
            }
        }

        composable(route = "add_driver") {
            LaunchedEffect(Unit) {
                availableTransports = transportViewModel.getAllAvailableTransports()
            }
            if (isAvailableTransportListLoading) {
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

        composable(route = "transports") {
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

fun NavHostController.navigateSingleToTop(route: String) =
    this.navigate(route) {
        restoreState = true
        launchSingleTop = true
    }