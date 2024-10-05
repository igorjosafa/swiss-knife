package com.example.swissknife.presentation.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import com.example.swissknife.R
import com.example.swissknife.domain.calculator.Add
import com.example.swissknife.domain.calculator.Div
import com.example.swissknife.domain.calculator.Mul
import com.example.swissknife.domain.calculator.Sub


class CalculatorActivity : AppCompatActivity() {

    private val viewModel: CalculatorViewModel by viewModels {
        CalculatorViewModelFactory(Add(), Sub(), Mul(), Div())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val typeInputView = findViewById<TextView>(R.id.typeInput)
        val fixedInputView = findViewById<TextView>(R.id.fixedInput)
        val operatorView = findViewById<TextView>(R.id.operator)
        val resultButton = findViewById<Button>(R.id.buttonEq)

        val numberButtons = listOf<Button>(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9),
            findViewById(R.id.button0),
            findViewById(R.id.buttonMil),
        )


        for (numberButton in numberButtons) {
            numberButton.setOnClickListener { view ->
                val buttonText = (view as Button).text.toString()
                val resultViewText = typeInputView.text.toString()
                if (buttonText == ".") {
                    if ("." !in resultViewText) {
                        typeInputView.text = buildString {
                            append(resultViewText)
                            append(buttonText)
                        }
                    }
                }
                else {
                    typeInputView.text = buildString {
                        append(resultViewText)
                        append(buttonText)
                    }
                }
            }
        }

        val operatorButtons = listOf<Button>(
            findViewById(R.id.buttonAdd),
            findViewById(R.id.buttonSub),
            findViewById(R.id.buttonDiv),
            findViewById(R.id.buttonMul),
        )

        for (operatorButton in operatorButtons) {
            operatorButton.setOnClickListener { view ->
                val operation = (view as Button).text.toString()
                val typeInput = typeInputView.text.toString()
                val fixedInput = fixedInputView.text.toString()

                if (typeInput != "" && typeInput != "-" && fixedInput == "") {
                    fixedInputView.text = typeInput
                    operatorView.text = operation
                    typeInputView.text = ""
                }
                else if (typeInput != "" && operation != "" && fixedInput != "") {
                    resultButton.performClick()
                    fixedInputView.text = typeInputView.text.toString()
                    operatorView.text = operation
                    typeInputView.text = ""
                }
            }
        }

        resultButton.setOnClickListener { _ ->
            val fixedInput = fixedInputView.text.toString()
            val operation = operatorView.text.toString()
            val typeInput = typeInputView.text.toString()
            var result = 0.0

            if (typeInput != "" && typeInput != "-" && operation != "" && fixedInput != "") {
                when (operation) {
                    "+" -> result = viewModel.adicionar(fixedInput.toDouble(), typeInput.toDouble())
                    "-" -> result = viewModel.subtrair(fixedInput.toDouble(), typeInput.toDouble())
                    "÷" -> result = viewModel.dividir(fixedInput.toDouble(), typeInput.toDouble())
                    "X" -> result = viewModel.multiplicar(fixedInput.toDouble(), typeInput.toDouble())
                }

                typeInputView.text = result.toString()
                operatorView.text = ""
                fixedInputView.text = ""
            }
        }

        val clearButton = findViewById<Button>(R.id.buttonCE)
        clearButton.setOnClickListener { view ->
            fixedInputView.text = ""
            operatorView.text = ""
            typeInputView.text = ""
        }

        val posNeg = findViewById<Button>(R.id.buttonPosNeg)
        posNeg.setOnClickListener { view ->
            var typeInputText = typeInputView.text.toString()
            if (typeInputText.startsWith("-")) {
                typeInputView.text = typeInputText.substring(1)
            } else {
                typeInputView.text = buildString {
                    append("-")
                    append(typeInputText)
                }
            }
        }

    }

}
