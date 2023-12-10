package com.example.mybooks.data

import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getAllBooksStream(): Flow<List<Book>>

    fun getTotalBooksStream(): Int

    fun getBookStream(user: String): Flow<Book>

    suspend fun insertBook(item: Book)

    suspend fun deleteBook(item: Book)

    suspend fun updateBook(item: Book)

    fun getFavoriteBooksStream(): Flow<List<Book>>

    fun getReadBooksStream(): Flow<List<Book>>



}