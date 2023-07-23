package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.Division


@CalculateDsl
class DivisionBuilder(private var result: Double?) {

    var dividend: Double = 0.0
    var divisor: Double = 0.0

    fun build(): Division {
        if (result != null) {
            return Division(result!!, divisor)
        }
        return Division(dividend, divisor)
    }

}
