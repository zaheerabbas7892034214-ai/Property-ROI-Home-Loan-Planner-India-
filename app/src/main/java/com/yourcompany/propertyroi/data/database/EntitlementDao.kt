package com.yourcompany.propertyroi.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yourcompany.propertyroi.data.models.Entitlement
import kotlinx.coroutines.flow.Flow

@Dao
interface EntitlementDao {
    @Query("SELECT * FROM entitlements WHERE id = 1")
    fun getEntitlement(): Flow<Entitlement?>
    
    @Query("SELECT * FROM entitlements WHERE id = 1")
    suspend fun getEntitlementOnce(): Entitlement?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntitlement(entitlement: Entitlement)
    
    @Update
    suspend fun updateEntitlement(entitlement: Entitlement)
}
