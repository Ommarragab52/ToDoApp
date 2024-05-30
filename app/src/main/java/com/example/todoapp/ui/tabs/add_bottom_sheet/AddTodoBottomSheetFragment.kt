package com.example.todoapp.ui.tabs.add_bottom_sheet

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.database.model.Todo
import com.example.todoapp.databinding.FragmentAddBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar


class AddTodoBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var viewBinding: FragmentAddBottomSheetBinding
    private lateinit var viewModel: AddTodoViewModel
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAddBottomSheetBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[AddTodoViewModel::class.java]
        viewBinding.viewModel = viewModel
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        calendar = Calendar.getInstance()
        viewBinding.selectTimeValue.text =
            "${calendar.get(Calendar.DAY_OF_MONTH)} / " +
                    "${calendar.get(Calendar.MONTH) + 1} / " +
                    "${calendar.get(Calendar.YEAR)}"
        viewBinding.selectTimeValue.setOnClickListener {
            showDatePickerDialog()
        }
        viewBinding.addTaskButton.setOnClickListener {
            viewModel.addTaskToDatabase(calendar){
                onTaskAddedListener?.onAddedTask()
                dismiss()
            }
        }
    }




    var onTaskAddedListener: OnTaskAddedListener? = null

    fun interface OnTaskAddedListener {
        fun onAddedTask()
    }

    @SuppressLint("SetTextI18n")
    fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(requireContext())
        datePicker.show()
        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            viewBinding.selectTimeValue.text = "$dayOfMonth / ${month + 1}/ $year"
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

}