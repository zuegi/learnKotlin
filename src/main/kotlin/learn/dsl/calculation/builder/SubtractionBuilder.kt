package learn.dsl.calculation.builder

import learn.dsl.calculation.model.operation.Subtraction


class SubtractionBuilder {

    var minuend: Double = 0.0
    var subtrahend: Double = 0.0

    fun build(): Subtraction = Subtraction(minuend, subtrahend)

}
