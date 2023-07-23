package learn.dsl.calculation.builder

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.Subtraction


@CalculateDsl
class SubtractionBuilder(private var result: Double?) {

    var minuend: Double = 0.0
    var subtrahend: Double = 0.0

    fun build(): Subtraction {
        if (result != null) {
            return Subtraction(result!!, subtrahend)
        }
        return Subtraction(minuend, subtrahend)
    }


}
