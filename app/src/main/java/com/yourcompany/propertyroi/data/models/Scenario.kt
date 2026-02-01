package com.yourcompany.propertyroi.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scenarios")
data class Scenario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val inputsJson: String, // JSON format for flexibility
    val createdAt: Long = System.currentTimeMillis()
)
