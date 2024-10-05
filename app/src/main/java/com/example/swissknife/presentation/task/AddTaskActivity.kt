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
import com.example.swissknife.domain.task.Task
import com.example.swissknife.domain.task.DealWithNotifications.Companion.scheduleNotification
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        taskViewModel.createDB(this)

        val scheduleDateEditText = findViewById<TextView>(R.id.scheduleDate)
        val timeEditText = findViewById<TextView>(R.id.notificatonTime)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                scheduleDateEditText.text = selectedDate
            },
            year, month, day
        )

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeEditText.text = formattedTime
            },
            hour, minute, true
        )

        timeEditText.setOnClickListener {
            timePickerDialog.show()
        }

        scheduleDateEditText.setOnClickListener {
            datePickerDialog.show()
        }

        findViewById<Button>(R.id.addTaskButton).setOnClickListener {
            val taskName = findViewById<TextInputEditText>(R.id.taskName).text.toString()
            val taskDescription = findViewById<EditText>(R.id.taskDescription).text.toString()
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

            val defaultId = 0
            val newTask = Task(defaultId, taskName, taskDescription, creationDate, null,
                scheduleDate, notificationTime)

            val taskId = taskViewModel.addTask(newTask)

            if (notificationTime != null && scheduleDate != null) {

                val notificationStr = "$scheduleDateStr $notificationTimeStr:00"
                val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale
                    .getDefault()).parse(notificationStr)
                if (date != null) {
                    scheduleNotification(this, taskId, taskName, date)
                }
            }

            val intentTaskActivity = Intent(this, TaskActivity::class.java)
            startActivity(intentTaskActivity)

        }
    }
}
