package com.example.swissknife.domain.calculator;



interface CalculatorUseCases <T, R> {
    fun result(input: T): R
}