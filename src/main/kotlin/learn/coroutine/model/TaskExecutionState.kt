package learn.coroutine.model

enum class TaskExecutionState {
    INITIAL,
    RUNNING,
    COMPLETED,
    CANCELLED,
    ERROR,
}
