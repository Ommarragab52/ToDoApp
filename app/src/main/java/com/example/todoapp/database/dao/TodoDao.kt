package com.example.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.model.Todo
import java.util.Date

//Data Access Object
@Dao
interface TodoDao {
    @Insert
    fun insertTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): List<Todo>

    @Query("SELECT * FROM todo_table WHERE time = :date")
    fun getTodoByDate(date: Date):List<Todo>
}