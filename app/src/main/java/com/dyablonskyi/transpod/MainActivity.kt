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
import com.dyablonskyi.transpod.ui.MainViewModel
import com.dyablonskyi.transpod.ui.screen.admin.AdminScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverInsertScreen
import com.dyablonskyi.transpod.ui.screen.admin.driver.DriverListScreen
import com.dyablonskyi.transpod.ui.screen.admin.route.RouteListScreen
import com.dyablonskyi.transpod.ui.screen.admin.transport.TransportListScreen
import com.dyablonskyi.transpod.ui.screen.main.MainScreen
import com.dyablonskyi.transpod.ui.screen.user.UserScreen
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
    viewModel: MainViewModel = viewModel()
) {
    val tickets by viewModel.tickets.collectAsState()
    val routes by viewModel.routes.collectAsState()
    val drivers by viewModel.drivers.collectAsState()
    val transports by viewModel.transports.collectAsState()
    val availableTransports by viewModel.availableTransports.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable(route = "main_screen") {
            LaunchedEffect(Unit) {
                viewModel.loadAll()
            }
            if (isLoading) {
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
            LaunchedEffect(Unit) {
                viewModel.loadTickets()
            }

            if (isLoading) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                UserScreen(
                    tickets = tickets,
                    onBuyButtonClick = { duration ->
                        viewModel.insertTicket(TicketBuilder(duration, null).build())
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
            LaunchedEffect(Unit) {
                viewModel.loadRoutes()
            }
            if (isLoading) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                RouteListScreen(
                    routes = routes,
                    onInsertButtonClick = {
                        viewModel.insertRoute(it)
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
            LaunchedEffect(Unit) {
                viewModel.loadDrivers()
            }
            if (isLoading) {
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
                viewModel.loadAvailableTransports()
            }
            if (isLoading) {
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
                        viewModel.insertDriver(it)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = "transports") {
            LaunchedEffect(Unit) {
                viewModel.loadTransports()
            }
            if (isLoading) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                TransportListScreen(
                    transports = transports,
                    onInsertButtonClick = { transport ->
                        viewModel.insertTransport(transport)
                    },
                    showToastMessage = { errorMsg ->
                        Toast.makeText(
                            context,
                            errorMsg,
                            Toast.LENGTH_SHORT
                        ).show()
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