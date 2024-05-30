package com.example.todoapp

import android.app.Application
import com.example.todoapp.database.TodoDatabase

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TodoDatabase.init(this)
    }
}