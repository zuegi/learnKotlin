package learn.coroutine.domain.task

import learn.coroutine.backgroundTask
import learn.coroutine.domain.BaseTask
import learn.coroutine.model.TaskExecutionResult
import learn.coroutine.model.TaskExecutionSuccess
import learn.dsl.calculation.calculate

class SequentialAdditionTask : BaseTask() {
    suspend fun execute(
        bid: Double,
        ask: Double
    ): TaskExecutionResult = backgroundTask {

        val calculation = calculate {
            operation {
                addition {
                    summand1 = bid
                    summand2 = ask
                }
                subtraction {
                    subtrahend = 0.1
                }
            }
        }

        println("This is my calculation: ${calculation.calculate()}")

        return@backgroundTask TaskExecutionSuccess(1000L)
    }
}
