package com.yourcompany.propertyroi

import android.app.Application
import com.yourcompany.propertyroi.billing.BillingManager
import com.yourcompany.propertyroi.data.database.AppDatabase
import com.yourcompany.propertyroi.data.repository.EntitlementRepository
import com.yourcompany.propertyroi.data.repository.ScenarioRepository

class PropertyRoiApplication : Application() {
    
    val database by lazy { AppDatabase.getDatabase(this) }
    val scenarioRepository by lazy { ScenarioRepository(database.scenarioDao()) }
    val entitlementRepository by lazy { EntitlementRepository(database.entitlementDao()) }
    
    lateinit var billingManager: BillingManager
    
    override fun onCreate() {
        super.onCreate()
        billingManager = BillingManager(this, entitlementRepository)
    }
}
