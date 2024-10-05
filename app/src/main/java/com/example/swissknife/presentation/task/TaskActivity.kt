package com.example.swissknife.presentation.task

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.swissknife.R
import com.example.swissknife.data.task.DatabaseHelper
import com.example.swissknife.domain.calculator.Add
import com.example.swissknife.domain.task.Task
import java.text.SimpleDateFormat
import java.util.Date


class TaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskList : List<Task>
    private lateinit var taskListContainer : LinearLayout

    private fun showTasks() {
        setContentView(R.layout.activity_task)

        taskViewModel.createDB(this)
        taskList = taskViewModel.loadTasks(emptyList())

        taskList = taskList.sortedWith(compareBy(nullsLast()) { it.scheduleDate })

        taskListContainer = findViewById(R.id.taskListContainer)
        taskListContainer.removeAllViews()

        for (task in taskList) {
            val horizontalLayout = LinearLayout(this)
            val button = Button(this)
            val delButton = ImageButton(this)

            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            horizontalLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            button.id = task.id
            button.text = task.name
            button.layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
            button.setOnClickListener {
                val intent = Intent(this, UpdateTaskActivity::class.java)
                intent.putExtra("id", task.id)
                startActivity(intent)
            }

            delButton.setImageResource(R.drawable.ic_delete)
            delButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            delButton.setOnClickListener {
                val intent = Intent(this, DeleteTaskActivity::class.java)
                intent.putExtra("id", task.id)
                startActivity(intent)
            }

            horizontalLayout.addView(button)
            horizontalLayout.addView(delButton)
            taskListContainer.addView(horizontalLayout)

            }

        findViewById<Button>(R.id.addTaskButton).setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onRestart() {
        super.onRestart()
        showTasks()
    }

    override fun onResume() {
        super.onResume()
        showTasks()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showTasks()
    }
}