package com.yourcompany.propertyroi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.propertyroi.billing.BillingManager
import com.yourcompany.propertyroi.data.models.Entitlement
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    billingManager: BillingManager,
    entitlementFlow: StateFlow<Entitlement?>,
    onNavigateBack: () -> Unit
) {
    val entitlement by entitlementFlow.collectAsState()
    val purchaseState by billingManager.purchaseState.collectAsState()
    var showRestoreMessage by remember { mutableStateOf(false) }
    
    LaunchedEffect(purchaseState) {
        if (purchaseState is BillingManager.PurchaseState.Success) {
            showRestoreMessage = true
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                    text = "Purchase",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Pro Status",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (entitlement?.hasPro == true) "Active" else "Not Active",
                                    color = if (entitlement?.hasPro == true) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            if (entitlement?.hasPro != true) {
                                Button(
                                    onClick = { billingManager.restorePurchases() },
                                    enabled = purchaseState !is BillingManager.PurchaseState.Loading
                                ) {
                                    if (purchaseState is BillingManager.PurchaseState.Loading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Text("Restore")
                                    }
                                }
                            }
                        }
                        
                        if (showRestoreMessage) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = when (purchaseState) {
                                    is BillingManager.PurchaseState.Success -> "Purchase restored successfully!"
                                    is BillingManager.PurchaseState.Error -> (purchaseState as BillingManager.PurchaseState.Error).message
                                    else -> ""
                                },
                                color = when (purchaseState) {
                                    is BillingManager.PurchaseState.Success -> MaterialTheme.colorScheme.primary
                                    is BillingManager.PurchaseState.Error -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.onSurface
                                },
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                Text(
                    text = "About",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("App Name")
                            Text("Property ROI & Home Loan Planner")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Version")
                            Text("1.0")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Package")
                            Text("com.yourcompany.propertyroi")
                        }
                    }
                }
            }
        }
    }
}
