package com.yourcompany.propertyroi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.propertyroi.data.models.*
import com.yourcompany.propertyroi.utils.CalculatorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RentalYieldViewModel : ViewModel() {
    
    private val _propertyPrice = MutableStateFlow("")
    val propertyPrice: StateFlow<String> = _propertyPrice.asStateFlow()
    
    private val _monthlyRent = MutableStateFlow("")
    val monthlyRent: StateFlow<String> = _monthlyRent.asStateFlow()
    
    private val _maintenance = MutableStateFlow("")
    val maintenance: StateFlow<String> = _maintenance.asStateFlow()
    
    private val _tax = MutableStateFlow("")
    val tax: StateFlow<String> = _tax.asStateFlow()
    
    private val _result = MutableStateFlow<RentalYieldResult?>(null)
    val result: StateFlow<RentalYieldResult?> = _result.asStateFlow()
    
    fun updatePropertyPrice(value: String) {
        _propertyPrice.value = value
    }
    
    fun updateMonthlyRent(value: String) {
        _monthlyRent.value = value
    }
    
    fun updateMaintenance(value: String) {
        _maintenance.value = value
    }
    
    fun updateTax(value: String) {
        _tax.value = value
    }
    
    fun calculateYield() {
        viewModelScope.launch {
            try {
                val priceValue = _propertyPrice.value.toDoubleOrNull() ?: return@launch
                val rentValue = _monthlyRent.value.toDoubleOrNull() ?: return@launch
                val maintenanceValue = _maintenance.value.toDoubleOrNull() ?: 0.0
                val taxValue = _tax.value.toDoubleOrNull() ?: 0.0
                
                if (priceValue <= 0 || rentValue <= 0) return@launch
                
                val input = RentalYieldInput(
                    propertyPrice = priceValue,
                    monthlyRent = rentValue,
                    annualMaintenance = maintenanceValue,
                    propertyTax = taxValue
                )
                
                _result.value = CalculatorUtils.calculateRentalYield(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
