package com.example.mybooks.data

import kotlinx.coroutines.flow.Flow

class OfflineBooksRepository(private val bookDao: BookDao): BooksRepository {
    override fun getAllBooksStream(): Flow<List<Book>> = bookDao.getAllBooks()

    override fun getTotalBooksStream(): Int = bookDao.getTotalBooks()

    override fun getBookStream(username: String): Flow<Book> = bookDao.getUser(username)

    override suspend fun insertBook(item: Book) = bookDao.insert(item)

    override suspend fun deleteBook(item: Book) = bookDao.delete(item)

    override suspend fun updateBook(item: Book) = bookDao.update(item)

    override fun getFavoriteBooksStream(): Flow<List<Book>> = bookDao.getFavoriteBooks()

    override fun getReadBooksStream(): Flow<List<Book>> = bookDao.getReadBooks()

}