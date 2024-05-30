package com.example.todoapp.ui.tabs.todo_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.R
import com.example.todoapp.database.model.Todo
import com.example.todoapp.databinding.ItemTodoBinding
import com.zerobranch.layout.SwipeLayout.SwipeActionsListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale


class TodoListAdapter(var todoList: MutableList<Todo>?) :
    Adapter<TodoListAdapter.TodoListViewHolder>() {

    class TodoListViewHolder(val itemTodoBinding: ItemTodoBinding) :
        ViewHolder(itemTodoBinding.root) {
        fun bind(todoModel: Todo) {
            val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = format.format(todoModel.time)
            itemTodoBinding.taskTime.text = formattedDate
            itemTodoBinding.todoModel = todoModel
            itemTodoBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val itemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoListViewHolder(itemTodoBinding)
    }

    override fun getItemCount(): Int {
        return todoList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(todoList: MutableList<Todo>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    var onItemDeleteClick: OnItemDeleteClick? = null
    var onItemLongClick: OnItemLongClick? = null
    var onTaskDone: OnTaskDone? = null

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onBindViewHolder(
        holder: TodoListViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = todoList?.get(position)
        holder.bind(item ?: return)

        if (item.isDone == true) {
            holder.itemTodoBinding.taskCheckButton.setImageResource(R.drawable.ic_done)
            holder.itemTodoBinding.taskCheckButton.setBackgroundResource(R.color.transparent)
            holder.itemTodoBinding.viewLine.setBackgroundColor(
                holder.itemTodoBinding.viewLine.resources.getColor(R.color.green, null)
            )
            holder.itemTodoBinding.taskName.setTextColor(
                holder.itemTodoBinding.taskName.resources.getColor(R.color.green, null)
            )
        } else if (item.isDone == false) {
            holder.itemTodoBinding.taskCheckButton.setImageResource(R.drawable.ic_check)
            holder.itemTodoBinding.taskCheckButton.setBackgroundResource(R.drawable.is_done_button_shape)
            holder.itemTodoBinding.viewLine.setBackgroundColor(
                holder.itemTodoBinding.viewLine.resources.getColor(R.color.blue_light, null)
            )
            holder.itemTodoBinding.taskName.setTextColor(
                holder.itemTodoBinding.taskName.resources.getColor(R.color.blue_light, null)
            )
        }
        holder.itemTodoBinding.swipe.setOnActionsListener(object : SwipeActionsListener {
            override fun onOpen(direction: Int, isContinuous: Boolean) {
                holder.itemTodoBinding.delete.setOnClickListener {
                    holder.itemTodoBinding.swipe.close(true)
                    onItemDeleteClick?.onClickDelete(item, position)
                }
            }

            override fun onClose() {
            }

        })
        holder.itemTodoBinding.taskCheckButton.setOnClickListener {
            onTaskDone?.onDone(item)
        }
        if (onItemLongClick != null) {
            holder.itemTodoBinding.card.setOnLongClickListener {
                onItemLongClick?.onLongClick(item)
                true
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
        todoList?.removeAt(position)
        notifyDataSetChanged()
    }

    fun interface OnTaskDone {
        fun onDone(todo: Todo)
    }

    fun interface OnItemDeleteClick {
        fun onClickDelete(todo: Todo, position: Int)
    }

    fun interface OnItemLongClick {
        fun onLongClick(todo: Todo)
    }

}