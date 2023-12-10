package com.example.mybooks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * from books_table ORDER BY username ASC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT COUNT(*) FROM books_table")
    fun getTotalBooks(): Int

    @Query("SELECT * from books_table WHERE username = :username")
    fun getUser(username: String): Flow<Book>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(book: Book)

    @Update
    suspend fun update(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("SELECT * FROM books_table WHERE isFavorite == 1")
    fun getFavoriteBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books_table WHERE isRead == 1")
    fun getReadBooks(): Flow<List<Book>>

    @Update
    suspend fun updateBook(book: Book)
}
