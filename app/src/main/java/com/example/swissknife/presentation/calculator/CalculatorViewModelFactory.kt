package com.example.swissknife.presentation.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.swissknife.domain.calculator.Add
import com.example.swissknife.domain.calculator.Div
import com.example.swissknife.domain.calculator.Mul
import com.example.swissknife.domain.calculator.Sub

class CalculatorViewModelFactory(
    private val add: Add,
    private val sub: Sub,
    private val mul: Mul,
    private val div: Div,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(add, sub, mul, div) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}