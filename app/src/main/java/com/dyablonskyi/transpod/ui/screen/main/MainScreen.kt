package com.dyablonskyi.transpod.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

@Composable
fun MainScreen(
    onUserButtonClick: () -> Unit,
    onAdminButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign in as ...",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(50.dp))
            Button(
                onClick = onUserButtonClick,
                modifier = Modifier.width(200.dp)
            ) {
                Text(text = "User")
            }
            Spacer(modifier = Modifier.size(20.dp))
            Button(
                onClick = onAdminButtonClick,
                modifier = Modifier.width(200.dp)
            ) {
                Text(text = "Admin")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    TranspodTheme {
        MainScreen({}, {})
    }
}