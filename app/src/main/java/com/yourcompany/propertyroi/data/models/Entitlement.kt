package com.yourcompany.propertyroi.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entitlements")
data class Entitlement(
    @PrimaryKey
    val id: Int = 1,
    val hasPro: Boolean = false,
    val purchaseToken: String? = null,
    val purchaseTime: Long = 0
)
