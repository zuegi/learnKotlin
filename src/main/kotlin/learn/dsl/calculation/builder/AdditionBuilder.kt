package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.Addition


@CalculateDsl
class AdditionBuilder(private var result: Double?) {

    var summand1: Double = 0.0
    var summand2: Double = 0.0

    fun build(): Addition {
        if (result != null) {
            return Addition(result!!, summand2)
        }
        return Addition(summand1, summand2)
    }
}
