package com.example.updatd_mahila_shakthi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User entity for local Room-based authentication.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)
