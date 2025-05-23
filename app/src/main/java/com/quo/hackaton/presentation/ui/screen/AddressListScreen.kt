package com.quo.hackaton.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.quo.hackaton.domain.model.Address
import com.quo.hackaton.domain.model.Status
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressListScreen(
    addresses: List<Address>,
    onStatusChange: (Address, Status) -> Unit,
    onShowMap: () -> Unit
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Адреса") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onShowMap) {
                Icon(Icons.Default.LocationOn, contentDescription = "Карта")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(addresses) { address ->
                AddressRow(address, onStatusChange)
            }
        }
    }
}

@Composable
fun AddressRow(address: Address, onStatusChange: (Address, Status) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(address.title)
            Text(address.status.name, style = MaterialTheme.typography.bodySmall)
        }
        Row {
            Button(onClick = { onStatusChange(address, Status.OK) }) { Text("OK") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onStatusChange(address, Status.VIOLATION) }) { Text("!") }
        }
    }
}