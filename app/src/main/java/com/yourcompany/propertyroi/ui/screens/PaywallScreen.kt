package com.yourcompany.propertyroi.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.propertyroi.billing.BillingManager
import com.yourcompany.propertyroi.data.models.Entitlement
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallScreen(
    billingManager: BillingManager,
    entitlementFlow: StateFlow<Entitlement?>,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    val productDetails by billingManager.productDetails.collectAsState()
    val purchaseState by billingManager.purchaseState.collectAsState()
    val entitlement by entitlementFlow.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upgrade to Pro") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (entitlement?.hasPro == true) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onTertiary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "You're a Pro!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                                Text(
                                    text = "All features unlocked",
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Unlock Pro Features",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "₹349 (One-time payment)",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            
            item {
                ProFeatureItem("Export PDF Reports")
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                ProFeatureItem("Save Unlimited Scenarios")
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                ProFeatureItem("Compare Scenarios Side-by-Side")
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                ProFeatureItem("Advanced Projections with Detailed Analytics")
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            if (entitlement?.hasPro != true) {
                item {
                    when (purchaseState) {
                        is BillingManager.PurchaseState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is BillingManager.PurchaseState.Error -> {
                            Text(
                                text = (purchaseState as BillingManager.PurchaseState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Button(
                                onClick = {
                                    activity?.let { billingManager.launchPurchaseFlow(it) }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Purchase Pro - ₹349")
                            }
                        }
                        else -> {
                            Button(
                                onClick = {
                                    activity?.let { billingManager.launchPurchaseFlow(it) }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = productDetails != null
                            ) {
                                Text("Purchase Pro - ₹349")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProFeatureItem(feature: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = feature,
            fontSize = 16.sp
        )
    }
}
