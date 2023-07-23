package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.Calculation
import learn.dsl.calculation.model.operation.CalcOperation

@CalculateDsl
class CalculationBuilder {


    private var operations = mutableListOf<CalcOperation>()
    private var result: Double = 0.0

    fun build(): Calculation = Calculation(operations)

    fun operation(block: CalcOperationBuilder.() -> Unit) {
        val calcOperation = CalcOperationBuilder().apply(block).build()
        result += calcOperation.calculate()
        operations.add(calcOperation)

    }

}
