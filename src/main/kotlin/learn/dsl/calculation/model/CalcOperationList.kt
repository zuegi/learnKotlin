package learn.dsl.calculation.model

import learn.dsl.calculation.CalculateDsl
import learn.dsl.calculation.model.operation.CalcOperation

@CalculateDsl
class CalcOperationList: ArrayList<CalcOperation>() {

/*    fun operation(block: CalcOperationBuilder.() -> Unit) {
      *//*  add(CalcOperationBuilder().apply(block).build())*//*
    }*/

}
