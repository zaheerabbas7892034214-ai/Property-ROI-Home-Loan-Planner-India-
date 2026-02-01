package com.yourcompany.propertyroi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourcompany.propertyroi.data.models.Scenario
import com.yourcompany.propertyroi.data.repository.ScenarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val scenarioRepository: ScenarioRepository) : ViewModel() {
    
    private val _scenarios = MutableStateFlow<List<Scenario>>(emptyList())
    val scenarios: StateFlow<List<Scenario>> = _scenarios.asStateFlow()
    
    init {
        loadScenarios()
    }
    
    private fun loadScenarios() {
        viewModelScope.launch {
            scenarioRepository.getAllScenarios().collect { scenarios ->
                _scenarios.value = scenarios
            }
        }
    }
    
    fun deleteScenario(scenario: Scenario) {
        viewModelScope.launch {
            scenarioRepository.deleteScenario(scenario)
        }
    }
}
