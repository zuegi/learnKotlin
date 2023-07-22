package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.CalcOperation

@CalculateDsl
class CalcOperationBuilder {

    var value1: Double = 0.0
    var value2: Double = 0.0
    lateinit var operation: CalcOperation


    fun addition(block: AdditionBuilder.() -> Unit): Double {
       operation = AdditionBuilder().apply(block).build()
       return operation.calculate()
    }

    fun multiplikation(block: MultiplicationBuilder.() -> Unit): Double {
        operation = MultiplicationBuilder().apply(block).build()
        return operation.calculate()
    }

    fun division(block: DivisionBuilder.() -> Unit): Double {
        operation = DivisionBuilder().apply(block).build()
        return operation.calculate()
    }

    fun subtraction(block: SubtractionBuilder.() -> Unit): Double {
        operation = SubtractionBuilder().apply(block).build()
        return operation.calculate()
    }

    fun build(): CalcOperation = operation

}
