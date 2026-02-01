package com.yourcompany.propertyroi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.propertyroi.data.models.*
import com.yourcompany.propertyroi.utils.CalculatorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoanEmiViewModel : ViewModel() {
    
    private val _principal = MutableStateFlow("")
    val principal: StateFlow<String> = _principal.asStateFlow()
    
    private val _interestRate = MutableStateFlow("")
    val interestRate: StateFlow<String> = _interestRate.asStateFlow()
    
    private val _tenure = MutableStateFlow("")
    val tenure: StateFlow<String> = _tenure.asStateFlow()
    
    private val _result = MutableStateFlow<LoanEmiResult?>(null)
    val result: StateFlow<LoanEmiResult?> = _result.asStateFlow()
    
    fun updatePrincipal(value: String) {
        _principal.value = value
    }
    
    fun updateInterestRate(value: String) {
        _interestRate.value = value
    }
    
    fun updateTenure(value: String) {
        _tenure.value = value
    }
    
    fun calculateEmi() {
        viewModelScope.launch {
            try {
                val principalValue = _principal.value.toDoubleOrNull() ?: return@launch
                val rateValue = _interestRate.value.toDoubleOrNull() ?: return@launch
                val tenureValue = _tenure.value.toIntOrNull() ?: return@launch
                
                if (principalValue <= 0 || rateValue <= 0 || tenureValue <= 0) return@launch
                
                val input = LoanEmiInput(
                    principal = principalValue,
                    interestRate = rateValue,
                    tenureYears = tenureValue
                )
                
                _result.value = CalculatorUtils.calculateLoanEmi(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
