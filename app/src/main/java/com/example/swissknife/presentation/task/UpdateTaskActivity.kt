package com.example.swissknife.presentation.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.swissknife.R
import com.example.swissknife.domain.task.DealWithNotifications
import com.example.swissknife.domain.task.Task
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)
        taskViewModel.createDB(this)

        val taskNameView = findViewById<EditText>(R.id.taskName)
        val taskDescriptionView = findViewById<EditText>(R.id.taskDescription)
        val scheduleDateEditText = findViewById<TextView>(R.id.scheduleDate)
        val timeEditText = findViewById<TextView>(R.id.notificatonTime)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val taskId = intent.getIntExtra("id", 0)

        val task = taskViewModel.loadTasks(listOf(taskId))

        taskNameView.setText(task[0].name)
        taskDescriptionView.setText(task[0].description)
        scheduleDateEditText.setText(task[0].scheduleDate?.let { dateFormat.format(it) })
        val h = task[0].notificationTime?.let { it/100 }
        val min = task[0].notificationTime?.let { it%100 }
        if (task[0].notificationTime != null) {
            timeEditText.text = String.format("%02d:%02d", h, min)
        }

        timeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    timeEditText.text = formattedTime
                },
                hour, minute, true
            )

            timePickerDialog.show()
        }

        scheduleDateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    scheduleDateEditText.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.updateTaskButton).setOnClickListener {
            val taskName = taskNameView.text.toString()
            val taskDescription = taskDescriptionView.text.toString()
            val creationDate: Date = Calendar.getInstance().time
            val scheduleDateStr: String = scheduleDateEditText.text.toString()
            val scheduleDate: Date? = try{
                dateFormat.parse(scheduleDateStr)
            } catch (_: Exception) {
                null
            }
            val notificationTimeStr: String = timeEditText.text.toString()
            val notificationTime: Int? = try{
                notificationTimeStr.replace(":", "").toInt()
            } catch (_: Exception) {
                null
            }

            val taskToUpdate = Task(taskId, taskName, taskDescription, creationDate, null,
                scheduleDate, notificationTime)

            taskViewModel.updateTask(taskToUpdate)

            if (notificationTime != null && scheduleDate != null) {

                val notificationStr = "$scheduleDateStr $notificationTimeStr:00"
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale
                    .getDefault()).parse(notificationStr)
                if (date != null) {
                    try {
                        DealWithNotifications.cancelNotification(this, taskId, taskName, date)
                    } catch (_: Exception) {}
                    DealWithNotifications.scheduleNotification(this, taskId, taskName, date)
                }
            }

            val intentTaskActivity = Intent(this, TaskActivity::class.java)
            startActivity(intentTaskActivity)
        }
    }

}
