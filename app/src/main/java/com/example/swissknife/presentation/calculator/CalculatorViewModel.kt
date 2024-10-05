package com.example.swissknife.presentation.calculator

import androidx.lifecycle.ViewModel
import com.example.swissknife.domain.calculator.Add
import com.example.swissknife.domain.calculator.Div
import com.example.swissknife.domain.calculator.Mul
import com.example.swissknife.domain.calculator.Sub

class CalculatorViewModel(
    private val add: Add,
    private val sub: Sub,
    private val mul: Mul,
    private val div: Div
    ) : ViewModel() {

    fun adicionar(a: Double, b: Double): Double {
        return add.result(Pair(a, b));
    }

    fun subtrair(a: Double, b: Double): Double {
        return sub.result(Pair(a, b));
    }

    fun multiplicar(a: Double, b: Double): Double {
        return mul.result(Pair(a, b));
    }

    fun dividir(a: Double, b: Double): Double {
        return div.result(Pair(a, b));
    }
}