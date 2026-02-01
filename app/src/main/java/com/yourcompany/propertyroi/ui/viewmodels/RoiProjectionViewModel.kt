package com.yourcompany.propertyroi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.propertyroi.data.models.*
import com.yourcompany.propertyroi.utils.CalculatorUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoiProjectionViewModel : ViewModel() {
    
    private val _propertyValue = MutableStateFlow("")
    val propertyValue: StateFlow<String> = _propertyValue.asStateFlow()
    
    private val _monthlyRent = MutableStateFlow("")
    val monthlyRent: StateFlow<String> = _monthlyRent.asStateFlow()
    
    private val _appreciationRate = MutableStateFlow("")
    val appreciationRate: StateFlow<String> = _appreciationRate.asStateFlow()
    
    private val _rentEscalation = MutableStateFlow("")
    val rentEscalation: StateFlow<String> = _rentEscalation.asStateFlow()
    
    private val _vacancyRate = MutableStateFlow("")
    val vacancyRate: StateFlow<String> = _vacancyRate.asStateFlow()
    
    private val _maintenanceCost = MutableStateFlow("")
    val maintenanceCost: StateFlow<String> = _maintenanceCost.asStateFlow()
    
    private val _stampDuty = MutableStateFlow("")
    val stampDuty: StateFlow<String> = _stampDuty.asStateFlow()
    
    private val _result = MutableStateFlow<RoiProjectionResult?>(null)
    val result: StateFlow<RoiProjectionResult?> = _result.asStateFlow()
    
    fun updatePropertyValue(value: String) {
        _propertyValue.value = value
    }
    
    fun updateMonthlyRent(value: String) {
        _monthlyRent.value = value
    }
    
    fun updateAppreciationRate(value: String) {
        _appreciationRate.value = value
    }
    
    fun updateRentEscalation(value: String) {
        _rentEscalation.value = value
    }
    
    fun updateVacancyRate(value: String) {
        _vacancyRate.value = value
    }
    
    fun updateMaintenanceCost(value: String) {
        _maintenanceCost.value = value
    }
    
    fun updateStampDuty(value: String) {
        _stampDuty.value = value
    }
    
    fun calculateRoi() {
        viewModelScope.launch {
            try {
                val propertyVal = _propertyValue.value.toDoubleOrNull() ?: return@launch
                val rentVal = _monthlyRent.value.toDoubleOrNull() ?: return@launch
                val appreciationVal = _appreciationRate.value.toDoubleOrNull() ?: return@launch
                val escalationVal = _rentEscalation.value.toDoubleOrNull() ?: return@launch
                val vacancyVal = _vacancyRate.value.toDoubleOrNull() ?: 0.0
                val maintenanceVal = _maintenanceCost.value.toDoubleOrNull() ?: 0.0
                val stampVal = _stampDuty.value.toDoubleOrNull() ?: 0.0
                
                if (propertyVal <= 0 || rentVal <= 0) return@launch
                
                val input = RoiProjectionInput(
                    propertyValue = propertyVal,
                    monthlyRent = rentVal,
                    appreciationRate = appreciationVal,
                    rentEscalationRate = escalationVal,
                    vacancyRate = vacancyVal,
                    maintenanceCostRate = maintenanceVal,
                    stampDutyRegistration = stampVal,
                    projectionYears = 10
                )
                
                _result.value = CalculatorUtils.calculateRoiProjection(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
