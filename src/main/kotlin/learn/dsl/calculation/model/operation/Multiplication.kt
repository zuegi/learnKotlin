package learn.dsl.calculation.model.operation

class Multiplication(
   private val factor1: Double,
    private val factor2: Double
): CalcOperation {

    override fun calculate(): Double {
        return factor1 * factor2
    }
}
