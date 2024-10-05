package com.example.swissknife.presentation.task

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.swissknife.R
import com.example.swissknife.domain.task.DealWithNotifications
import java.text.SimpleDateFormat
import java.util.Locale

class DeleteTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    private fun backToTaskActivity() {
        intent = Intent(this, TaskActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_task)
        taskViewModel.createDB(this)


        val taskId = intent.getIntExtra("id", 0)
        val task = taskViewModel.loadTasks(listOf(taskId))

        val taskNameView = findViewById<TextView>(R.id.deleteTaskName)
        taskNameView.text = task[0].name
        val taskDescriptionView = findViewById<TextView>(R.id.deleteTaskDescription)
        taskDescriptionView.text = task[0].description

        findViewById<Button>(R.id.yesButton).setOnClickListener {
            taskViewModel.deleteTask(taskId)
            val taskNotificationDateStr = task[0].scheduleDate?.let { SimpleDateFormat("dd/MM/yyyy")
                .format(it) }
            val h = task[0].notificationTime?.let { it/100 }
            val min = task[0].notificationTime?.let { it%100 }

            if (taskNotificationDateStr != null && h != null) {
                try {
                    val dateStr = taskNotificationDateStr + " " + String.format("%02d:%02d", h, min)
                    val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(dateStr)
                    DealWithNotifications.cancelNotification(this, taskId, task[0].name, date)
                } catch (_: Exception) {}
            }
            backToTaskActivity()

        }

        findViewById<Button>(R.id.noButton).setOnClickListener {
            backToTaskActivity()
        }

    }
}