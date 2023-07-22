package learn.dsl.calculation.model.operation

class Subtraction(
    val minuend: Double,
    val subtrahend: Double
): CalcOperation {
    override fun calculate(): Double {
       return minuend - subtrahend
    }
}
