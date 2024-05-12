package com.dyablonskyi.transpod.ui.screen.user.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dyablonskyi.transpod.ui.theme.TranspodTheme
import java.time.LocalDateTime

@Composable
fun TicketItem(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    price: Double,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(15.dp)
            .shadow(3.dp, MaterialTheme.shapes.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$$price",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(5.dp)
                    .width(70.dp)
            )
            Divider(
                Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${startDate.toStringFullDateFormat()}\n-\n${endDate.toStringFullDateFormat()}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketItemPreview() {
    TranspodTheme {
        TicketItem(
            startDate = LocalDateTime.of(2024, 5, 6, 18, 0, 0),
            endDate = LocalDateTime.of(2024, 6, 6, 18, 0, 0),
            price = 100.0
        )
    }
}

fun LocalDateTime.toStringFullDateFormat() =
    String.format("%02d.%02d.%d at %02d:%02d", dayOfMonth, monthValue, year, hour, minute)

fun LocalDateTime.toStringHourFormat() =
    String.format("%02d:%02d", hour, minute)
