package com.dyablonskyi.transpod.ui.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

@Composable
fun AdminScreen(
    onDriverButtonClick: () -> Unit,
    onTransportButtonClick: () -> Unit,
    onRouteButtonClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = onDriverButtonClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = "Drivers")
        }
        Button(
            onClick = onTransportButtonClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = "Transports")
        }
        Button(
            onClick = onRouteButtonClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = "Routes")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminScreenPreview() {
    TranspodTheme {
        AdminScreen(
            onDriverButtonClick = { /*TODO*/ },
            onTransportButtonClick = { /*TODO*/ },
            onRouteButtonClick = {/*TODO*/ }
        )
    }
}