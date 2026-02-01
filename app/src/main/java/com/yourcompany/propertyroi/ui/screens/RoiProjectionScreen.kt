package com.yourcompany.propertyroi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.yourcompany.propertyroi.ui.viewmodels.RoiProjectionViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoiProjectionScreen(
    viewModel: RoiProjectionViewModel,
    onNavigateBack: () -> Unit
) {
    val propertyValue by viewModel.propertyValue.collectAsState()
    val monthlyRent by viewModel.monthlyRent.collectAsState()
    val appreciationRate by viewModel.appreciationRate.collectAsState()
    val rentEscalation by viewModel.rentEscalation.collectAsState()
    val vacancyRate by viewModel.vacancyRate.collectAsState()
    val maintenanceCost by viewModel.maintenanceCost.collectAsState()
    val stampDuty by viewModel.stampDuty.collectAsState()
    val result by viewModel.result.collectAsState()
    
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ROI Projection") },
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
                    text = "Investment Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            item {
                OutlinedTextField(
                    value = propertyValue,
                    onValueChange = { viewModel.updatePropertyValue(it) },
                    label = { Text("Property Value (₹)") },
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
                    value = appreciationRate,
                    onValueChange = { viewModel.updateAppreciationRate(it) },
                    label = { Text("Annual Appreciation (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = rentEscalation,
                    onValueChange = { viewModel.updateRentEscalation(it) },
                    label = { Text("Rent Escalation (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = vacancyRate,
                    onValueChange = { viewModel.updateVacancyRate(it) },
                    label = { Text("Vacancy Rate (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = maintenanceCost,
                    onValueChange = { viewModel.updateMaintenanceCost(it) },
                    label = { Text("Maintenance Cost (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = stampDuty,
                    onValueChange = { viewModel.updateStampDuty(it) },
                    label = { Text("Stamp Duty & Registration (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Button(
                    onClick = { viewModel.calculateRoi() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate ROI")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            result?.let { roiResult ->
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
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Internal Rate of Return (IRR)",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = String.format("%.2f%%", roiResult.irr),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                item {
                    Text(
                        text = "10-Year Projection",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                
                items(roiResult.timeline) { projection ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Year ${projection.year}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Property Value", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(projection.propertyValue), fontSize = 11.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Annual Rent", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(projection.annualRent), fontSize = 11.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Net Cash Flow", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(projection.netCashFlow), fontSize = 11.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Cumulative Return", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(projection.cumulativeReturn), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
