package com.yourcompany.propertyroi.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object LoanEmi : Screen("loan_emi")
    object RentalYield : Screen("rental_yield")
    object RoiProjection : Screen("roi_projection")
    object Compare : Screen("compare")
    object Export : Screen("export")
    object Paywall : Screen("paywall")
    object Settings : Screen("settings")
}
