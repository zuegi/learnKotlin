package learn.dsl.calculation.builder

import learn.dsl.calculation.model.Calculation
import learn.dsl.calculation.model.operation.CalcOperation

class CalculationBuilder {


    var operations = mutableListOf<CalcOperation>()

    fun build(): Calculation = Calculation(operations)
    fun operation(block: CalcOperationBuilder.() -> Unit) {
        operations.add(CalcOperationBuilder().apply(block).build())
    }

}