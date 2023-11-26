package com.example.mybooks.data

import kotlinx.coroutines.flow.Flow

class OfflineUsersRepository(private val userDao: UserDao): UsersRepository {
    override fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()

    override fun getTotalUsersStream(): Int = userDao.getTotalUsers()

    override fun getUserStream(username: String): Flow<User> = userDao.getUser(username)

    override suspend fun insertUser(item: User) = userDao.insert(item)

    override suspend fun deleteUser(item: User) = userDao.delete(item)

    override suspend fun updateUser(item: User) = userDao.update(item)
}