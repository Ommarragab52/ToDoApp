package com.example.todoapp.ui.tabs.add_bottom_sheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.database.model.Todo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTodoViewModel : ViewModel() {
    var titleLiveData = MutableLiveData<String>()
    var descriptionLiveData = MutableLiveData<String>()
    var titleErrorLiveData = MutableLiveData<String?>()
    var descriptionErrorLiveData = MutableLiveData<String?>()

    private fun validateFields(): Boolean {
        if (titleLiveData.value.isNullOrEmpty() || titleLiveData.value.isNullOrBlank()) {
            titleErrorLiveData.value = "Required title"
            return false
        } else titleErrorLiveData.value = null

        if (descriptionLiveData.value?.isEmpty() == true) {
            descriptionErrorLiveData.value = "Required title"
            return false
        } else descriptionErrorLiveData.value = null
        return true
    }

    fun addTaskToDatabase(calendar: Calendar, onTaskAdded: () -> Unit) {
        if (validateFields()) {
            val time = calendar.time
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = format.format(time)
            val date = format.parse(formattedDate)
            TodoDatabase
                .getInstance()
                .getTodoDao
                .insertTodo(
                    Todo(
                        title = titleLiveData.value,
                        description = descriptionLiveData.value,
                        isDone = false,
                        time = date
                    )
                )
            onTaskAdded()
        }
    }

}