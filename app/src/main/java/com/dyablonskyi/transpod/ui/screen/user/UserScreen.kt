package com.dyablonskyi.transpod.ui.screen.user

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dyablonskyi.transpod.data.local.db.entity.Duration
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.TicketBuilder
import com.dyablonskyi.transpod.ui.screen.user.ticket.BuyTicketDialog
import com.dyablonskyi.transpod.ui.screen.user.ticket.TicketItem
import com.dyablonskyi.transpod.ui.theme.TranspodTheme

@Composable
fun UserScreen(
    tickets: List<Ticket>,
    onBuyButtonClick: (Duration) -> Unit,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            Text(
                text = "Welcome User 👋",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.size(30.dp))
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TicketList(tickets)
        }
        AnimatedVisibility(showDialog) {
            BuyTicketDialog(
                onBuyButtonClick = {
                    onBuyButtonClick(it)
                    showDialog = false
                },
                onDismissRequest = { showDialog = false }
            )
        }
    }
}

@Composable
fun TicketList(
    tickets: List<Ticket> = emptyList()
) {
    Column {
        /*Text(
            text = "Your tickets",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 20.dp)
        )*/
        LazyColumn {
            items(
                items = tickets,
                key = { ticket -> ticket.id}
            ) {
                TicketItem(
                    startDate = it.startDate,
                    endDate = it.endDate,
                    price = it.price
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenPreview() {
    TranspodTheme {
        UserScreen(
            listOf(
                TicketBuilder(Duration.YEAR, null).build(),
                TicketBuilder(Duration.HOUR, null).build(),
                TicketBuilder(Duration.DAY, null).build(),
                TicketBuilder(Duration.MONTH, null).build(),
                TicketBuilder(Duration.MONTH, null).build(),
                TicketBuilder(Duration.MONTH, null).build(),
            )
        ) {}
    }
}