package com.example.mybooks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybooks.data.User
import com.example.mybooks.data.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {

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
}