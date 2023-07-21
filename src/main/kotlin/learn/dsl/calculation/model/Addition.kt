package learn.dsl.calculation.model

class Addition(
    private val summand1: Double,
    private val summand2: Double
): CalcOperation {

    override fun calculate(): Double {
        return summand1 + summand2
    }

}
