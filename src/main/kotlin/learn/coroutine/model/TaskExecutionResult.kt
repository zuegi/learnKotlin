package learn.coroutine.model


sealed class TaskExecutionResult
data class TaskExecutionSuccess(val result: Long) : TaskExecutionResult()
//data class TaskExecutionError(val exception: CustomTaskException) : TaskExecutionResult()
object TaskExecutionCancelled : TaskExecutionResult()
