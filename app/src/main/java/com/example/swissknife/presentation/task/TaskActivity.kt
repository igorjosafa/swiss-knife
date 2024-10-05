package com.example.swissknife.presentation.task

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.swissknife.R
import com.example.swissknife.data.task.DatabaseHelper
import com.example.swissknife.domain.calculator.Add
import com.example.swissknife.domain.task.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskList : List<Task>
    private lateinit var taskListContainer : LinearLayout

    private fun showTasks() {
        setContentView(R.layout.activity_task)

        taskViewModel.createDB(this)
        taskList = taskViewModel.loadTasks(emptyList())

        val groupedTasks = taskList.groupBy { it.scheduleDate }

        taskListContainer = findViewById(R.id.taskListContainer)
        taskListContainer.removeAllViews()

        for ((scheduleDate, tasks) in groupedTasks) {
            val groupLayout = LinearLayout(this)
            groupLayout.orientation = LinearLayout.VERTICAL
            groupLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            groupLayout.setPadding(16, 16, 16, 16)

            val dateText = TextView(this).apply {
                text = scheduleDate?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                } ?: "Sem data"
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
            }

            groupLayout.addView(dateText)

            for (task in tasks) {
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
                groupLayout.addView(horizontalLayout)

            }
            taskListContainer.addView(groupLayout)
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