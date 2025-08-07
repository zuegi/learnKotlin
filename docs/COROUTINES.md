# Kotlin Coroutines & Concurrency 🔄

This section demonstrates advanced coroutine patterns, custom scopes, and concurrent programming techniques in Kotlin.

## 📁 File Structure

```
learn/coroutine/
├── MathScope.kt                    # Custom CoroutineScope implementation
├── MathScopeConfiguration.kt       # Dispatcher configuration
├── MathScopeCoroutinesHelper.kt    # Core coroutine utilities
├── MathScopeExtension.kt           # Extension functions for DSL
├── domain/
│   ├── BaseTask.kt                 # Abstract base for tasks
│   └── task/
│       ├── SequentialAdditionTask.kt    # Example task implementation
│       └── SequentialTaskUseCase.kt     # Use case pattern
├── model/
│   ├── TaskExecutionResult.kt      # Result wrapper
│   └── TaskExecutionState.kt       # State management
└── repository/
    └── RemoteRepository.kt         # Async data access
```

## 🎯 Learning Objectives

- **Custom CoroutineScope**: Create domain-specific coroutine scopes
- **Dispatcher Management**: Abstract different execution contexts
- **Error Handling**: Proper cancellation and exception handling
- **Timeout Strategies**: Configurable timeout patterns
- **Extension Functions**: Create DSL-like APIs for coroutines

## 🚀 Key Features

### 1. Custom CoroutineScope (`MathScope.kt`)

```kotlin
open class MathScope : CoroutineScope {
    private val job = Job()
    
    override val coroutineContext: CoroutineContext
        get() = MathScopeConfiguration.uiDispatcher + job
}
```

**Benefits:**
- Domain-specific scope for math operations
- Centralized job management
- Easy cancellation of all related coroutines

### 2. Configurable Dispatchers (`MathScopeConfiguration.kt`)

```kotlin
class MathScopeConfiguration {
    companion object {
        var uiDispatcher: CoroutineDispatcher = Dispatchers.Main
        var backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default
        var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        
        var isDelayEnabled: Boolean = true
        var useTestTimeout: Boolean = false
    }
}
```

**Features:**
- Runtime dispatcher switching
- Test-friendly configuration
- Delay control for testing

### 3. Extension Functions DSL (`MathScopeExtension.kt`)

```kotlin
// Fire-and-forget jobs
fun CoroutineScope.uiJob(
    timeout: Long = 0L,
    block: suspend CoroutineScope.() -> Unit,
)

// Tasks that return values
suspend fun <T> uiTask(
    timeout: Long = 0L,
    block: suspend CoroutineScope.() -> T,
): T

// Async tasks with Deferred
fun <T> CoroutineScope.uiTaskAsync(
    timeout: Long = 0L,
    block: suspend CoroutineScope.() -> T,
): Deferred<T>
```

### 4. Advanced Error Handling

```kotlin
suspend fun <T> Deferred<T>.awaitOrReturn(returnIfCancelled: T): T =
    try {
        await()
    } catch (e: CancellationException) {
        returnIfCancelled
    }

suspend fun <T> awaitAllOrCancel(vararg deferreds: Deferred<T>): List<T> {
    try {
        return awaitAll(*deferreds)
    } catch (e: Exception) {
        if (e !is CancellationException) {
            deferreds.forEach { if (it.isActive) it.cancel() }
        }
        throw e
    }
}
```

## 📝 Usage Examples

### Basic Usage

```kotlin
class MyService : MathScope() {
    
    fun processData() {
        // UI operations
        uiJob {
            println("Updating UI...")
            delay(100)
        }
        
        // Background computation
        backgroundJob {
            val result = heavyComputation()
            println("Result: $result")
        }
        
        // I/O operations
        ioJob {
            val data = fetchFromNetwork()
            saveToDatabase(data)
        }
    }
}
```

### With Timeout

```kotlin
suspend fun fetchDataWithTimeout() {
    val result = ioTask(timeout = 5000L) {
        // This will timeout after 5 seconds
        fetchDataFromServer()
    }
}
```

### Async Operations

```kotlin
suspend fun parallelProcessing() {
    val deferred1 = backgroundTaskAsync { computeValue1() }
    val deferred2 = backgroundTaskAsync { computeValue2() }
    val deferred3 = backgroundTaskAsync { computeValue3() }
    
    // Wait for all results or cancel all if one fails
    val results = awaitAllOrCancel(deferred1, deferred2, deferred3)
    println("Results: $results")
}
```

### Error Handling

```kotlin
suspend fun robustOperation() {
    val deferred = backgroundTaskAsync { 
        riskyOperation()
    }
    
    val result = deferred.awaitOrReturn("default_value")
    println("Result or default: $result")
}
```

## 🧪 Testing

The framework includes comprehensive testing support:

### Test Configuration

```kotlin
@BeforeEach
fun setup() {
    MathScopeConfiguration.uiDispatcher = Dispatchers.Unconfined
    MathScopeConfiguration.backgroundDispatcher = Dispatchers.Unconfined
    MathScopeConfiguration.ioDispatcher = Dispatchers.Unconfined
    MathScopeConfiguration.useTestTimeout = true
    MathScopeConfiguration.isDelayEnabled = false
}
```

### Test Examples

```kotlin
@Test
fun `should execute background task`() = runTest {
    var executed = false
    
    backgroundJob {
        executed = true
    }
    
    advanceUntilIdle()
    assertTrue(executed)
}

@Test
fun `should handle timeout`() = runTest {
    assertThrows<TimeoutCancellationException> {
        uiTask(timeout = 100L) {
            delay(200L) // This will timeout
        }
    }
}
```

## 🎛️ Configuration Options

### Dispatcher Configuration
- `uiDispatcher`: For UI updates (default: `Dispatchers.Main`)
- `backgroundDispatcher`: For CPU-intensive work (default: `Dispatchers.Default`)
- `ioDispatcher`: For I/O operations (default: `Dispatchers.IO`)

### Testing Configuration
- `useTestTimeout`: Use shorter timeouts for tests
- `isDelayEnabled`: Enable/disable delays for testing
- `TEST_TIMEOUT`: Default test timeout (500ms)

## 🔍 Advanced Patterns

### 1. Task Execution Result Pattern

```kotlin
sealed class TaskExecutionResult<out T> {
    data class Success<T>(val value: T) : TaskExecutionResult<T>()
    data class Error(val exception: Exception) : TaskExecutionResult<Nothing>()
    object Cancelled : TaskExecutionResult<Nothing>()
}

suspend fun <T> executeTask(
    block: suspend () -> T
): TaskExecutionResult<T> = try {
    TaskExecutionResult.Success(block())
} catch (e: CancellationException) {
    TaskExecutionResult.Cancelled
} catch (e: Exception) {
    TaskExecutionResult.Error(e)
}
```

### 2. State Management

```kotlin
enum class TaskExecutionState {
    IDLE, RUNNING, COMPLETED, FAILED, CANCELLED
}

class StatefulTask {
    private val _state = MutableStateFlow(TaskExecutionState.IDLE)
    val state: StateFlow<TaskExecutionState> = _state.asStateFlow()
    
    suspend fun execute() {
        _state.value = TaskExecutionState.RUNNING
        try {
            // Task logic
            _state.value = TaskExecutionState.COMPLETED
        } catch (e: Exception) {
            _state.value = TaskExecutionState.FAILED
            throw e
        }
    }
}
```

## 📚 Key Concepts Demonstrated

1. **Custom CoroutineScope**: Domain-specific scope management
2. **Dispatcher Abstraction**: Clean separation of execution contexts
3. **Extension Functions**: DSL-like API creation
4. **Error Handling**: Comprehensive exception and cancellation handling
5. **Timeout Management**: Configurable timeout strategies
6. **Test Support**: Test-friendly configuration and utilities
7. **Structured Concurrency**: Proper parent-child coroutine relationships
8. **Resource Management**: Automatic cleanup and cancellation

## 🚨 Best Practices

1. **Always use structured concurrency**: Parent coroutines manage child coroutines
2. **Handle cancellation**: Use `CancellationException` appropriately
3. **Configure timeouts**: Prevent hanging operations
4. **Use appropriate dispatchers**: UI, Default, IO for different workloads
5. **Test async code**: Use `runTest` and proper test configurations
6. **Clean up resources**: Cancel jobs when no longer needed

## 🔗 Related Files

- **Tests**: `src/test/kotlin/learn/coroutine/`
- **Spring Integration**: `ch/zuegi/learnkotlin/` (reactive endpoints)
- **Actor Model**: `learn/actors/` (channel-based concurrency)

This coroutine framework demonstrates production-ready patterns for managing concurrency in Kotlin applications while maintaining testability and clean architecture principles.
