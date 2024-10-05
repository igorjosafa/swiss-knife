package com.example.swissknife.data.task

import com.example.swissknife.domain.task.Task
import com.example.swissknife.domain.task.TaskRepository

class DataRepositoryImpl(private val dbHelper : DatabaseHelper) : TaskRepository {
    override fun getTasks(ids: List<Int>): List<Task> {
        return dbHelper.getTasks(ids = ids)
    }

    override fun addTask(task: Task): Int {
        return dbHelper.addTask(
            task.name, task.description, task.creationDate, task.editDate,
            task.scheduleDate, task.notificationTime
        )
    }

    override fun updateTask(task: Task) {
        dbHelper.updateTask(task.id, task.name, task.description, task.creationDate, task
            .editDate, task.scheduleDate, task.notificationTime)
    }

    override fun deleteTask(id: Int) {
        dbHelper.deleteTask(id = id)
    }

}