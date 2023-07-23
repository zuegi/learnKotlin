package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.CalcOperation

@CalculateDsl
class CalcOperationBuilder {

    private lateinit var operation: CalcOperation
    var result: Double? = null


    fun addition(block: AdditionBuilder.() -> Unit): Double {
        operation = AdditionBuilder(result).apply(block).build()
        calculateResult()
        return operation.calculate()
    }

    private fun calculateResult() {
        if (result == null) {
           result = 0.0
        }
        result = result?.plus(operation.calculate())
    }

    fun multiplication(block: MultiplicationBuilder.() -> Unit): Double {
        operation = MultiplicationBuilder(result).apply(block).build()
        calculateResult()
        return operation.calculate()
    }

    fun division(block: DivisionBuilder.() -> Unit): Double {
        operation = DivisionBuilder(result).apply(block).build()
        calculateResult()
        return operation.calculate()
    }

    fun subtraction(block: SubtractionBuilder.() -> Unit): Double {
        operation = SubtractionBuilder(result).apply(block).build()
        calculateResult()
        return operation.calculate()
    }

    fun build(): CalcOperation = operation

}
