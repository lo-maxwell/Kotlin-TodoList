package com.example.todolist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter (
    private var todos:  MutableList<TodoItem>,
    private var db: TodoDBHelper,
    private var initFinished: Boolean = false,
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    fun addTODO(todoItem: TodoItem) {
        db.addItem(todoItem.title, todoItem.isChecked)
        todos.add(todoItem)
        notifyItemInserted(todos.size - 1)
    }

    fun deleteDoneTODOs() {
        db.deleteAllDoneItems()
        todos.removeAll{ todoItem ->
            todoItem.isChecked
        }
        notifyDataSetChanged()
    }

    fun loadAndParseDataOnInit() {
        if (initFinished) return
        todos = db.loadAndParseItemsOnInit()
        initFinished = true
    }

    private fun toggleStrikethrough(TextViewTODOTitle: TextView, isChecked: Boolean) {
        if(isChecked) {
            TextViewTODOTitle.paintFlags = TextViewTODOTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            TextViewTODOTitle.paintFlags = TextViewTODOTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodoItem = todos[position]
        holder.itemView.findViewById<TextView>(R.id.TextViewTODOTitle).text = currentTodoItem.title
        holder.itemView.apply{
//            findViewById<TextView>(R.id.TextViewTODOTitle).text = currentTodoItem.title
            findViewById<CheckBox>(R.id.CheckBoxDone).isChecked = currentTodoItem.isChecked
            toggleStrikethrough(findViewById<TextView>(R.id.TextViewTODOTitle), currentTodoItem.isChecked)
            findViewById<CheckBox>(R.id.CheckBoxDone).setOnCheckedChangeListener{ _, isCheckedLambda ->
                toggleStrikethrough(findViewById<TextView>(R.id.TextViewTODOTitle), isCheckedLambda)
                currentTodoItem.isChecked = !currentTodoItem.isChecked
                db.updateItemChecked(currentTodoItem.id, currentTodoItem.isChecked)
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}