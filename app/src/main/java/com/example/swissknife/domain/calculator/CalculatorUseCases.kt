package com.example.swissknife.domain.calculator

class Add : CalculatorUseCases<Pair<Double, Double>, Double> {
    override fun result(input: Pair<Double, Double>): Double {
        return input.first + input.second
    }
}

class Sub : CalculatorUseCases<Pair<Double, Double>, Double> {
    override fun result(input: Pair<Double, Double>): Double {
        return input.first - input.second
    }
}

class Mul : CalculatorUseCases<Pair<Double, Double>, Double> {
    override fun result(input: Pair<Double, Double>): Double {
        return input.first * input.second
    }
}

class Div : CalculatorUseCases<Pair<Double, Double>, Double> {
    override fun result(input: Pair<Double, Double>): Double {
        return input.first / input.second
    }
}