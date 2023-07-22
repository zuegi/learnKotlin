package learn.dsl.calculation.builder

import learn.dsl.calculation.model.operation.Addition

class AdditionBuilder {

    var summand1: Double = 0.0
    var summand2: Double = 0.0

    fun build(): Addition = Addition(summand1, summand2)
}
