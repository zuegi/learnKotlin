package learn.dsl.calculation.model.operation


class Division(
    private val dividend: Double,
    private val divisor: Double
): CalcOperation {
    override fun calculate(): Double {
        return dividend / divisor
    }
}
