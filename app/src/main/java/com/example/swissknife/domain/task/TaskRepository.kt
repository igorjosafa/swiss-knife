package com.example.swissknife.domain.task

import java.util.Date

interface TaskRepository {
    fun getTasks(ids: List<Int>): List<Task>
    fun addTask(task: Task): Int
    fun updateTask(task: Task)
    fun deleteTask(id: Int)
}

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val creationDate: Date,
    val editDate: Date?,
    val scheduleDate: Date?,
    val notificationTime: Int?
)

