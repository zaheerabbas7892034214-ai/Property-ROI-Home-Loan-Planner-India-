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
import com.yourcompany.propertyroi.ui.viewmodels.LoanEmiViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanEmiScreen(
    viewModel: LoanEmiViewModel,
    onNavigateBack: () -> Unit
) {
    val principal by viewModel.principal.collectAsState()
    val interestRate by viewModel.interestRate.collectAsState()
    val tenure by viewModel.tenure.collectAsState()
    val result by viewModel.result.collectAsState()
    
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Loan EMI") },
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
                    text = "Loan Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            item {
                OutlinedTextField(
                    value = principal,
                    onValueChange = { viewModel.updatePrincipal(it) },
                    label = { Text("Principal Amount (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = interestRate,
                    onValueChange = { viewModel.updateInterestRate(it) },
                    label = { Text("Interest Rate (% p.a.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                OutlinedTextField(
                    value = tenure,
                    onValueChange = { viewModel.updateTenure(it) },
                    label = { Text("Loan Tenure (Years)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Button(
                    onClick = { viewModel.calculateEmi() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate EMI")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            result?.let { emiResult ->
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
                            ResultRow("Monthly EMI", currencyFormat.format(emiResult.monthlyEmi))
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            ResultRow("Total Interest", currencyFormat.format(emiResult.totalInterest))
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            ResultRow("Total Payment", currencyFormat.format(emiResult.totalPayment))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                item {
                    Text(
                        text = "Amortization Schedule (First 12 Months)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                
                items(emiResult.amortizationSchedule.take(12)) { row ->
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
                                text = "Month ${row.month}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Principal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(row.principal), fontSize = 12.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Interest", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(row.interest), fontSize = 12.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Balance", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(currencyFormat.format(row.balance), fontSize = 12.sp)
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

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
