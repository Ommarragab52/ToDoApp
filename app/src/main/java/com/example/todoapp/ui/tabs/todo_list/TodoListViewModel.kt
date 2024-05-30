package com.example.todoapp.ui.tabs.todo_list

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.database.model.Todo
import java.util.Date

class TodoListViewModel : ViewModel() {
    val todoListLiveData = MutableLiveData<MutableList<Todo>>()
    fun getDataFromDatabase() {
        todoListLiveData.value = TodoDatabase.getInstance().getTodoDao.getAllTodos().toMutableList()
    }

    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodosByDay(date: Date? = null) {
        if (date != null) {
            val selectedDay = DateFormat.format("dd", date) as String
            val newList = mutableListOf<Todo>()
            val todosList = TodoDatabase.getInstance().getTodoDao.getAllTodos()
            for (todo in todosList) {
                val dayFromDatabase = DateFormat.format("dd", todo.time!!) as String
                if (dayFromDatabase == selectedDay)
                    newList.add(todo)
            }
            todoListLiveData.value = newList
        } else {
            todoListLiveData.value = TodoDatabase.getInstance()
                .getTodoDao.getAllTodos().toMutableList()
        }
    }

}