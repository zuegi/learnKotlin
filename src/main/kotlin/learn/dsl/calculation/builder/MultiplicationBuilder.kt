package learn.dsl.calculation.builder

import learn.dsl.calculation.model.Multiplication

class MultiplicationBuilder {

    var factor1: Double = 0.0
    var factor2: Double = 0.0

    fun build(): Multiplication = Multiplication(factor1, factor2)

}
