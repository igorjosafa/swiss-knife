package com.example.swissknife.presentation.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swissknife.data.task.DataRepositoryImpl
import com.example.swissknife.data.task.DatabaseHelper
import com.example.swissknife.domain.task.Task
import com.example.swissknife.domain.task.TaskRepository

class TaskViewModel() : ViewModel() {

    private lateinit var dbHelper : DatabaseHelper
    private lateinit var db : DataRepositoryImpl

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> get() = _taskList

    fun createDB(context : Context) {
        dbHelper = DatabaseHelper(context)
        dbHelper.writableDatabase
        db = DataRepositoryImpl(dbHelper)
    }

    fun loadTasks(ids: List<Int>) : List<Task> {
        return db.getTasks(ids = ids)
    }

    fun addTask(task: Task) : Int {
        val id = db.addTask(task)
        return id
    }

    fun updateTask(task: Task) {
        db.updateTask(task)
    }

    fun deleteTask(id: Int) {
        db.deleteTask(id)
    }
}