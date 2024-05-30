package com.example.todoapp.ui.tabs.todo_list

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.base.BaseFragment
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.database.model.Todo
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.ui.CONSTANTS
import com.example.todoapp.ui.EditActivity
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.WeekDayBinder
import com.zerobranch.layout.SwipeLayout
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale


class TodoListFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentListBinding
    private lateinit var viewModel: TodoListViewModel
    private lateinit var adapter: TodoListAdapter
    lateinit var calendar: Calendar
    var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TodoListViewModel::class.java]
        viewBinding.viewModel = viewModel
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initCalendarView()
        swipeToDelete()
        onTaskDone()
        onItemLongClick()
        viewModel.todoListLiveData.observe(viewLifecycleOwner) { todoList ->
            adapter.updateData(todoList)
        }
    }

    private fun onItemLongClick() {
        adapter.onItemLongClick =
            TodoListAdapter.OnItemLongClick { todo ->
                showMessage(
                    "what do you want ?",
                    "Update",
                    { _, _ -> updateTask(todo) },
                    "",
                )
            }
    }

    private fun updateTask(todo: Todo) {
        val intent = Intent(requireContext(), EditActivity::class.java)
        intent.putExtra(CONSTANTS.TASK, todo)
        startActivity(intent)
    }

    private fun onTaskDone() {
        adapter.onTaskDone = TodoListAdapter.OnTaskDone {
            it.isDone = true
            TodoDatabase.getInstance().getTodoDao.updateTodo(it)
            getDataFromDatabase()
        }
    }

    private fun swipeToDelete() {
        adapter.onItemDeleteClick =
            TodoListAdapter.OnItemDeleteClick { todo, position ->
                if (SwipeLayout(requireContext(), null).isEnabled) {
                    TodoDatabase.getInstance()
                        .getTodoDao
                        .deleteTodo(todo)
                    Snackbar.make(
                        viewBinding.todoListRecyclerView,
                        "Task deleted successfully",
                        Snackbar.LENGTH_LONG
                    ).setAction("Undo") {
                        TodoDatabase.getInstance()
                            .getTodoDao
                            .insertTodo(todo)
                        getDataFromDatabase()
                    }.show()
                    adapter.deleteItem(position)
                } else return@OnItemDeleteClick
            }

    }

    private fun initViews() {
        calendar = Calendar.getInstance()
        adapter = TodoListAdapter(null)
        viewBinding.todoListRecyclerView.adapter = adapter
        getDataFromDatabase()
    }

    fun getDataFromDatabase() {
        viewModel.getDataFromDatabase()
        adapter.updateData(viewModel.todoListLiveData.value ?: mutableListOf())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initCalendarView() {
        viewBinding.calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: WeekDay) {
                container.dayOfWeak.text = data.date.dayOfWeek
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())

                container.dayOfMonth.text = data.date.dayOfMonth.toString()
                container.calendarDayView.setOnClickListener {
                    if (selectedDate == data.date) {
                        selectedDate = null
                        viewModel.getTodosByDay(null)
                        viewBinding.calendarView.notifyCalendarChanged()
                    } else {
                        selectedDate = data.date
                        viewBinding.calendarView.notifyCalendarChanged()
                    }
                }
                if (selectedDate == data.date) {
                    container.dayOfWeak.setTextColor(resources.getColor(R.color.blue_light, null))
                    container.dayOfMonth.setTextColor(resources.getColor(R.color.blue_light, null))
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDate?.dayOfMonth!!)
                    viewModel.getTodosByDay(calendar.time)
                } else {
                    container.dayOfWeak.setTextColor(resources.getColor(R.color.black_light, null))
                    container.dayOfMonth.setTextColor(resources.getColor(R.color.black_light, null))

                }

            }

            override fun create(view: View): DayViewContainer {
                return DayViewContainer(view)
            }
        }

        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(100).atStartOfMonth() // Adjust as needed
        val endDate = currentMonth.plusMonths(100).atEndOfMonth()  // Adjust as needed
        // val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
        val dayOfWeek = daysOfWeek(DayOfWeek.of(calendar.get(Calendar.DAY_OF_WEEK)))
        viewBinding.calendarView.setup(startDate, endDate, dayOfWeek.last())
        viewBinding.calendarView.scrollToWeek(currentDate)
    }


}
