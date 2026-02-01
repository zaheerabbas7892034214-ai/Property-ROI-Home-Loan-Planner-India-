package com.yourcompany.propertyroi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourcompany.propertyroi.ui.navigation.Screen
import com.yourcompany.propertyroi.ui.screens.*
import com.yourcompany.propertyroi.ui.theme.PropertyROITheme
import com.yourcompany.propertyroi.ui.viewmodels.*

class MainActivity : ComponentActivity() {
    
    private val app by lazy { application as PropertyRoiApplication }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            PropertyROITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PropertyRoiNavigation()
                }
            }
        }
    }
    
    @Composable
    fun PropertyRoiNavigation() {
        val navController = rememberNavController()
        val entitlementFlow = app.entitlementRepository.getEntitlement()
        
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return HomeViewModel(app.scenarioRepository) as T
                        }
                    }
                )
                
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToLoanEmi = { navController.navigate(Screen.LoanEmi.route) },
                    onNavigateToYield = { navController.navigate(Screen.RentalYield.route) },
                    onNavigateToRoi = { navController.navigate(Screen.RoiProjection.route) },
                    onNavigateToPaywall = { navController.navigate(Screen.Paywall.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            
            composable(Screen.LoanEmi.route) {
                val loanEmiViewModel: LoanEmiViewModel = viewModel()
                
                LoanEmiScreen(
                    viewModel = loanEmiViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.RentalYield.route) {
                val rentalYieldViewModel: RentalYieldViewModel = viewModel()
                
                RentalYieldScreen(
                    viewModel = rentalYieldViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.RoiProjection.route) {
                val roiProjectionViewModel: RoiProjectionViewModel = viewModel()
                
                RoiProjectionScreen(
                    viewModel = roiProjectionViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.Paywall.route) {
                PaywallScreen(
                    billingManager = app.billingManager,
                    entitlementFlow = entitlementFlow,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    billingManager = app.billingManager,
                    entitlementFlow = entitlementFlow,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        app.billingManager.endConnection()
    }
}
