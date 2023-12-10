package com.example.mybooks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybooks.data.User
import com.example.mybooks.data.Book
import com.example.mybooks.data.UsersRepository
import com.example.mybooks.data.BooksRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository, private val booksRepository: BooksRepository) : ViewModel() {

    fun insertUser(id: Int, username: String, password: String) {
        viewModelScope.launch {
            usersRepository.insertUser(User(id, username, password))
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            usersRepository.getAllUsersStream()
        }
    }

    fun getTotalUsers() {
        viewModelScope.launch {
            usersRepository.getTotalUsers()
        }
    }

    fun getUser(username: String) {
        viewModelScope.launch {
            usersRepository.getUserStream(username)
        }
    }

    fun insertBook(book: Book) {
        viewModelScope.launch {
            booksRepository.insertBook(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            booksRepository.deleteBook(book)
        }
    }

    fun getAllBooks(): Flow<List<Book>> {
        return booksRepository.getAllBooksStream()
    }

    fun getFavoriteBooks(): Flow<List<Book>> {
        return booksRepository.getFavoriteBooksStream()
    }

    fun addToFavorites(book: Book) {
        viewModelScope.launch {
            val updatedBook = book.copy(isFavorite = 1)
            booksRepository.updateBook(updatedBook)
        }
    }

    fun deleteFromFavorites(book: Book) {
        viewModelScope.launch {
            val updatedBook = book.copy(isFavorite = 0)
            booksRepository.updateBook(updatedBook)
        }
    }

    fun getReadBooks(): Flow<List<Book>> {
        return booksRepository.getReadBooksStream()
    }

    fun addToRead(book: Book) {
        viewModelScope.launch {
            val updatedBook = book.copy(isRead = 1)
            booksRepository.updateBook(updatedBook)
        }
    }

    fun deleteFromRead(book: Book) {
        viewModelScope.launch {
            val updatedBook = book.copy(isRead = 0)
            booksRepository.updateBook(updatedBook)
        }
    }

}