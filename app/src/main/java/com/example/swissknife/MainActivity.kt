package com.example.swissknife


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.swissknife.presentation.calculator.CalculatorActivity
import com.example.swissknife.presentation.task.TaskActivity


class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCalculator = findViewById<Button>(R.id.calculator)
        val buttonTask = findViewById<Button>(R.id.task)

        buttonCalculator.setOnClickListener { _ ->
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
        }

        buttonTask.setOnClickListener { _ ->
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }
}