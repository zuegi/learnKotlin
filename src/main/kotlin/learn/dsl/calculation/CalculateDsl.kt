package learn.dsl.calculation

import learn.dsl.calculation.builder.CalculationBuilder
import learn.dsl.calculation.model.Calculation

@DslMarker
annotation class CalculateDsl

fun calculate(block: CalculationBuilder.() -> Unit): Calculation = CalculationBuilder().apply(block).build()
