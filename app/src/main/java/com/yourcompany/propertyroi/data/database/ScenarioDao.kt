package com.yourcompany.propertyroi.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yourcompany.propertyroi.data.models.Scenario
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenarioDao {
    @Query("SELECT * FROM scenarios ORDER BY createdAt DESC")
    fun getAllScenarios(): Flow<List<Scenario>>
    
    @Query("SELECT * FROM scenarios WHERE id = :id")
    suspend fun getScenarioById(id: Long): Scenario?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenario(scenario: Scenario): Long
    
    @Update
    suspend fun updateScenario(scenario: Scenario)
    
    @Delete
    suspend fun deleteScenario(scenario: Scenario)
    
    @Query("DELETE FROM scenarios WHERE id = :id")
    suspend fun deleteScenarioById(id: Long)
}
