package com.example.todoapp.ui.tabs.todo_list

import android.view.View
import android.widget.TextView
import com.example.todoapp.R
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(var calendarDayView: View) : ViewContainer(calendarDayView) {
    var dayOfWeak: TextView = calendarDayView.findViewById(R.id.day_of_weak)
    var dayOfMonth: TextView = calendarDayView.findViewById(R.id.day_of_month)
}