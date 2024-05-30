package com.example.todoapp.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.database.model.Todo
import com.example.todoapp.databinding.ActivityEditBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class EditActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityEditBinding
    private lateinit var calendar: Calendar
    private var task: Todo? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initViews()
        getDataFromIntent()
        updateData()
    }

    private fun updateData() {
        viewBinding.updateButton.setOnClickListener {
            if (!validate()) {
                return@setOnClickListener
            }
            task?.title = viewBinding.titleEditText.editText?.text.toString()
            task?.description = viewBinding.descriptionEditText.editText?.text.toString()
            task?.time = calendar.time
            TodoDatabase.getInstance().getTodoDao.updateTodo(task!!)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun validate(): Boolean {
        if (viewBinding.titleEditText.editText?.text.toString()
                .isBlank() || viewBinding.titleEditText.editText?.text.toString().isEmpty()
        ) {
            viewBinding.titleEditText.error = "Required title"
            return false
        } else viewBinding.titleEditText.error = null

        if (viewBinding.descriptionEditText.editText?.text.toString()
                .isBlank() || viewBinding.descriptionEditText.editText?.text.toString().isEmpty()
        ) {
            viewBinding.descriptionEditText.error = "Required description"
            return false
        } else viewBinding.descriptionEditText.error = null

        return true
    }

    private fun initViews() {
        calendar = Calendar.getInstance()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SimpleDateFormat")
    private fun getDataFromIntent() {
        task = intent.getSerializableExtra(CONSTANTS.TASK, Todo::class.java)
        viewBinding.titleEditText.editText?.setText(task?.title)
        viewBinding.descriptionEditText.editText?.setText(task?.description)
        val time = task?.time
        val format = SimpleDateFormat("yyyy/MM/dd")
        viewBinding.selectTimeValue.text = time?.let { format.format(it) }
        viewBinding.selectTimeValue.setOnClickListener {
            showDatePickerDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(this)
        datePicker.show()
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            viewBinding.selectTimeValue.text = "$year/$month/$dayOfMonth"
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }
}