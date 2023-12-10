package com.example.mybooks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "books_table")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val name: String,
    val author: String,
    val description: String,
    val isFavorite: Int = 0,
    val isRead: Int = 0
)
