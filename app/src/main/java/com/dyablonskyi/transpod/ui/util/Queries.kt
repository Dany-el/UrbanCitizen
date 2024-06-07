package com.dyablonskyi.transpod.ui.util

enum class DriverQuery(val stringItem: String){
    COUNT_DRIVER_WITHOUT_TRANSPORT("'Count driver' Query"),
    GET_DRIVERS_BY_ROUTE("'Get drivers' Query")
}

enum class RouteQuery(val stringItem: String){
    GET_ROUTES_BY_NAME("'Get routes by name' Query"),
    COUNT_DRIVERS_PER_INDIVIDUAL_ROUTE("'Count drivers per route' Query"),
    COUNT_TOTAL_DRIVERS("'Count total drivers' Query"),
    GET_ROUTES_WITH_TRANSPORT_TYPE_OF("'Get routes with transport type' Query"),
    RETURN("Return")
}

enum class TicketQuery(val stringItem: String){
    GET_TICKETS_BY_START_DATE_IN_DURATION("'Get tickets in duration' Query"),
    RETURN("Return")
}