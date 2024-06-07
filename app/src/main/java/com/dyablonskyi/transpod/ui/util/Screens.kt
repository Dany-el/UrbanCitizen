package com.dyablonskyi.transpod.ui.util

sealed class MainScreen(val route: String){
    data object Main: MainScreen("main_screen")
    data object Admin: MainScreen("admin")
    data object User: MainScreen("user")
}

sealed class ListScreen(val route: String){
    data object Routes: ListScreen("routes")
    data object Drivers: ListScreen("drivers")
    data object Transports: ListScreen("transports")
}

sealed class InsertScreen(val route: String){
    data object DriverInsert: InsertScreen("add_driver")
}