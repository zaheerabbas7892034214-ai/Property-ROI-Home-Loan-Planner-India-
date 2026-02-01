package com.yourcompany.propertyroi.data.repository

import com.yourcompany.propertyroi.data.database.EntitlementDao
import com.yourcompany.propertyroi.data.models.Entitlement
import kotlinx.coroutines.flow.Flow

class EntitlementRepository(private val entitlementDao: EntitlementDao) {
    
    fun getEntitlement(): Flow<Entitlement?> = entitlementDao.getEntitlement()
    
    suspend fun getEntitlementOnce(): Entitlement? = entitlementDao.getEntitlementOnce()
    
    suspend fun updateEntitlement(entitlement: Entitlement) {
        entitlementDao.insertEntitlement(entitlement)
    }
}
