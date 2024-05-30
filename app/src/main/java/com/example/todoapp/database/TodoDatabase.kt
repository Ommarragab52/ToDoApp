package com.example.todoapp.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.database.model.Todo

@Database(entities = [Todo::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoDatabase: RoomDatabase() {
     abstract val getTodoDao:TodoDao
    companion object {
        private var todoInstance: TodoDatabase? = null
        fun init(context: Context){
            if (todoInstance == null)
                todoInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
        fun getInstance(): TodoDatabase {
            return todoInstance!!
        }
    }
}
