package learn.dsl.calculation.model

import learn.dsl.calculation.model.operation.CalcOperation


class Calculation(
    private val operations: List<CalcOperation>
) {

    private var result: Double = 0.0

    fun calculate(): Double {

        for (operation in operations) {
            result += operation.calculate()
        }
        return result
    }

}
