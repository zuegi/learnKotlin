package learn.coroutine

import kotlinx.coroutines.*
import learn.logger
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.CoroutineContext

class MathScopeCoroutinesHelper {

    companion object {

        private val log by logger()
        private val loggingTaskId = AtomicLong(1L)

        private enum class HelperType {
            JOB, TASK, TASK_ASYNC
        }

        private suspend fun <T> executeBlock(
            parentScope: CoroutineScope,
            callerType: HelperType,
            coroutineContext: CoroutineContext,
            block: suspend CoroutineScope.() -> T
        ): T {

            val taskId = loggingTaskId.getAndIncrement()

            val methodName = when (coroutineContext) {
                MathScopeConfiguration.uiDispatcher -> "ui"
                MathScopeConfiguration.backgroundDispatcher -> "background"
                MathScopeConfiguration.ioDispatcher -> "io"
                else -> ""
            } + when (callerType) {
                HelperType.JOB -> "Job"
                HelperType.TASK -> "Task"
                HelperType.TASK_ASYNC -> "TaskAsync"
            } + "#$taskId"

            log.info("Started $methodName")
            val result = parentScope.block()
            log.info("Completed $methodName")
            return result

        }

        private fun computeTimeout(timeout: Long): Long {
            return if (MathScopeConfiguration.useTestTimeout) {
                MathScopeConfiguration.TEST_TIMEOUT
            } else {
                timeout
            }
        }


        fun startJob(
            parentScope: CoroutineScope,
            coroutineContext: CoroutineContext,
            timeout: Long = 0L,
            block: suspend CoroutineScope.() -> Unit
        ) {
            parentScope.launch(coroutineContext) {
                supervisorScope {
                    if (timeout > 0L) {
                        withTimeout(computeTimeout(timeout)) {
                            executeBlock(this, HelperType.JOB, coroutineContext, block)
                        }
                    } else {
                        executeBlock(this, HelperType.JOB, coroutineContext, block)
                    }
                }
            }
        }


        suspend fun <T> startTask(
            coroutineContext: CoroutineContext,
            timeout: Long = 0L,
            block: suspend CoroutineScope.() -> T
        ): T {
            return withContext(coroutineContext) {
                return@withContext if (timeout > 0L) {
                    withTimeout(computeTimeout(timeout)) {
                        executeBlock(this, HelperType.TASK, coroutineContext, block)
                    }
                } else {
                    executeBlock(this, HelperType.TASK, coroutineContext, block)
                }
            }
        }

        fun <T> startTaskAsync(
            parentScope: CoroutineScope,
            coroutineContext: CoroutineContext,
            timeout: Long = 0L,
            block: suspend CoroutineScope.() -> T
        ): Deferred<T> {
            return parentScope.async(coroutineContext) {
                return@async supervisorScope {
                    return@supervisorScope if (timeout > 0L) {
                        withTimeout(computeTimeout(timeout)) {
                            executeBlock(this, HelperType.TASK_ASYNC, coroutineContext, block)
                        }
                    } else {
                        executeBlock(this, HelperType.TASK_ASYNC, coroutineContext, block)
                    }
                }
            }
        }
    }
}
