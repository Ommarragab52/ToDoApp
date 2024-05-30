package com.example.todoapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.ui.tabs.add_bottom_sheet.AddTodoBottomSheetFragment
import com.example.todoapp.ui.tabs.todo_list.TodoListFragment
import com.example.todoapp.ui.tabs.settings.SettingsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var navigationBarView: NavigationBarView
    private lateinit var addTodoButton: FloatingActionButton
    private var todoListFragment: TodoListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        initViews()
        initNavigationBar()
        pushFragment(TodoListFragment())
        initAddTodoButton()
    }


    private fun initViews() {
        navigationBarView = findViewById(R.id.bottom_navigation_view)
        addTodoButton = findViewById(R.id.add_todo_button)
    }

    private fun initNavigationBar() {
        navigationBarView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item -> {
                    todoListFragment = TodoListFragment()
                    pushFragment(todoListFragment!!)
                }

                R.id.settings_item -> {
                    pushFragment(SettingsFragment())
                }
            }
            true
        }
        navigationBarView.selectedItemId = R.id.menu_item
    }

    private fun pushFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    @SuppressLint("ShowToast")
    private fun initAddTodoButton() {
        addTodoButton.setOnClickListener {
            navigationBarView.selectedItemId = R.id.menu_item
            val addTodoBottomSheetFragment = AddTodoBottomSheetFragment()
            addTodoBottomSheetFragment.onTaskAddedListener =
                AddTodoBottomSheetFragment.OnTaskAddedListener {
                    Snackbar.make(
                        coordinatorLayout,
                        "Task added successfully",
                        Snackbar.LENGTH_LONG
                    ).show()
                    todoListFragment?.getDataFromDatabase()
                }
            addTodoBottomSheetFragment.show(supportFragmentManager, null)
        }
    }

}