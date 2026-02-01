package com.yourcompany.propertyroi.data.repository

import com.yourcompany.propertyroi.data.database.ScenarioDao
import com.yourcompany.propertyroi.data.models.Scenario
import kotlinx.coroutines.flow.Flow

class ScenarioRepository(private val scenarioDao: ScenarioDao) {
    
    fun getAllScenarios(): Flow<List<Scenario>> = scenarioDao.getAllScenarios()
    
    suspend fun getScenarioById(id: Long): Scenario? = scenarioDao.getScenarioById(id)
    
    suspend fun insertScenario(scenario: Scenario): Long = scenarioDao.insertScenario(scenario)
    
    suspend fun updateScenario(scenario: Scenario) = scenarioDao.updateScenario(scenario)
    
    suspend fun deleteScenario(scenario: Scenario) = scenarioDao.deleteScenario(scenario)
    
    suspend fun deleteScenarioById(id: Long) = scenarioDao.deleteScenarioById(id)
}
