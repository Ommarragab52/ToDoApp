package com.example.todoapp.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity("todo_table")
class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var time: Date? = null,
    var isDone: Boolean? = false
):Serializable
