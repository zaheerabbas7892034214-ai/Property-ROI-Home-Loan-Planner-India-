package com.yourcompany.propertyroi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yourcompany.propertyroi.data.models.Entitlement
import com.yourcompany.propertyroi.data.models.Scenario

@Database(
    entities = [Scenario::class, Entitlement::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao
    abstract fun entitlementDao(): EntitlementDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "property_roi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
