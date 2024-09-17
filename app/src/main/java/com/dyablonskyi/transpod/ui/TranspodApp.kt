package com.dyablonskyi.transpod.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

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