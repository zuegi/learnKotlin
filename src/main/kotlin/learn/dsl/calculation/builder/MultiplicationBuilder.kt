package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.Multiplication

@CalculateDsl
class MultiplicationBuilder(private var result: Double?) {

    var factor1: Double = 0.0
    var factor2: Double = 0.0


    fun build(): Multiplication {
        if (result != null) {
            return Multiplication(result!!, factor2)
        }
        return  Multiplication(factor1, factor2)
    }

}
