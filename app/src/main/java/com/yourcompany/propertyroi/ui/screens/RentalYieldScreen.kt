package com.yourcompany.propertyroi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.propertyroi.ui.viewmodels.RentalYieldViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalYieldScreen(
    viewModel: RentalYieldViewModel,
    onNavigateBack: () -> Unit
) {
    val propertyPrice by viewModel.propertyPrice.collectAsState()
    val monthlyRent by viewModel.monthlyRent.collectAsState()
    val maintenance by viewModel.maintenance.collectAsState()
    val tax by viewModel.tax.collectAsState()
    val result by viewModel.result.collectAsState()
    
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rental Yield Calculator") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Property Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            item {
                OutlinedTextField(
                    value = propertyPrice,
                    onValueChange = { viewModel.updatePropertyPrice(it) },
                    label = { Text("Property Price (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = monthlyRent,
                    onValueChange = { viewModel.updateMonthlyRent(it) },
                    label = { Text("Monthly Rent (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = maintenance,
                    onValueChange = { viewModel.updateMaintenance(it) },
                    label = { Text("Annual Maintenance (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = tax,
                    onValueChange = { viewModel.updateTax(it) },
                    label = { Text("Property Tax (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Button(
                    onClick = { viewModel.calculateYield() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate Yield")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            result?.let { yieldResult ->
                item {
                    Text(
                        text = "Results",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            ResultRow("Annual Rent", currencyFormat.format(yieldResult.annualRent))
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            ResultRow("Annual Expenses", currencyFormat.format(yieldResult.annualExpenses))
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            ResultRow("Gross Yield", String.format("%.2f%%", yieldResult.grossYield))
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            ResultRow("Net Yield", String.format("%.2f%%", yieldResult.netYield))
                        }
                    }
                }
            }
        }
    }
}
