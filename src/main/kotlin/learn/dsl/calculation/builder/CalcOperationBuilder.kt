package learn.dsl.calculation.builder

import learn.dsl.calculation.model.Addition
import learn.dsl.calculation.model.CalcOperation
import learn.dsl.calculation.model.Multiplication

class CalcOperationBuilder {

    var value1: Double = 0.0
    var value2: Double = 0.0
    lateinit var operation: CalcOperation

    //    fun build(): Addition =  Addition(value1, value2)

    fun addition(block: AdditionBuilder.() -> Unit): Double {
       operation = AdditionBuilder().apply(block).build()
       return operation.calculate()
    }

    fun multiplikation(block: MultiplicationBuilder.() -> Unit): Double {
        operation = MultiplicationBuilder().apply(block).build()
        return operation.calculate()
    }

    fun build(): CalcOperation = operation

}
