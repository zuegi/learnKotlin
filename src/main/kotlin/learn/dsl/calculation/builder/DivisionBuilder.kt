package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.Division


@CalculateDsl
class DivisionBuilder {

    var dividend: Double = 0.0
    var divisor: Double = 0.0

    fun build(): Division = Division(dividend, divisor)

}
