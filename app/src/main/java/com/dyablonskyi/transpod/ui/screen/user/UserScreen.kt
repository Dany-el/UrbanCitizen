package com.dyablonskyi.transpod.ui.screen.user

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dyablonskyi.transpod.data.local.db.entity.Duration
import com.dyablonskyi.transpod.data.local.db.entity.Ticket
import com.dyablonskyi.transpod.data.local.db.entity.TicketBuilder
import com.dyablonskyi.transpod.ui.UIState
import com.dyablonskyi.transpod.ui.screen.user.ticket.BuyTicketDialog
import com.dyablonskyi.transpod.ui.screen.user.ticket.DateRangePicker
import com.dyablonskyi.transpod.ui.screen.user.ticket.TicketItem
import com.dyablonskyi.transpod.ui.screen.user.ticket.TicketViewModel
import com.dyablonskyi.transpod.ui.theme.TranspodTheme
import com.dyablonskyi.transpod.ui.util.TicketQuery
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    tickets: List<Ticket>,
    onBuyButtonClick: (Duration) -> Unit,
    onValueChange: (TicketQuery) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome, User 👋",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(20.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = { dropDownMenuExpanded = false }
                    ) {
                        TicketQuery.entries.forEach { query ->
                            DropdownMenuItem(
                                text = { Text(query.stringItem) },
                                onClick = {
                                    dropDownMenuExpanded = false
                                    onValueChange(query)
                                }
                            )
                        }
                    }
                }
            )
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
        if (showDialog) {
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
        LazyColumn {
            items(
                items = tickets,
                key = { ticket -> ticket.id }
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

@Composable
fun UserScreenWithDialog(
    ticketViewModel: TicketViewModel,
    uiState: UIState,
    context: Context = LocalContext.current
) {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    UserScreen(
        tickets = uiState.tickets,
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
        Box(
            Modifier.fillMaxSize()
        ) {
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
            ),
            {},
            {}
        )
    }
}