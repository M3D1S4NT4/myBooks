package com.example.mybooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybooks.data.UsersRepository
import com.example.mybooks.data.BooksRepository

class MainViewModelFactory(
    private val usersRepository: UsersRepository,
    private val booksRepository: BooksRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(usersRepository, booksRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
