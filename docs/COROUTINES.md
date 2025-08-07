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

## 🤔 What Are Coroutines?

**Coroutines** are lightweight threads that can be **suspended** and **resumed** without blocking the underlying thread. Think of them as:

- **Cooperative multitasking**: Functions that can pause themselves and let others run
- **Lightweight**: Millions of coroutines can run on a few threads
- **Sequential-looking code**: Write async code that looks synchronous

### Traditional Thread Problems
```kotlin
// ❌ Traditional blocking approach
fun fetchData(): String {
    Thread.sleep(1000) // Blocks the entire thread!
    return "data"
}

// This blocks the calling thread for 1 second
val result = fetchData()
```

### Coroutine Solution
```kotlin
// ✅ Coroutine approach
suspend fun fetchData(): String {
    delay(1000) // Suspends coroutine, not the thread!
    return "data"
}

// This doesn't block the thread - other coroutines can run
val result = fetchData()
```

## 🎯 Learning Objectives

- **Custom CoroutineScope**: Create domain-specific coroutine scopes
- **Dispatcher Management**: Abstract different execution contexts
- **Error Handling**: Proper cancellation and exception handling
- **Timeout Strategies**: Configurable timeout patterns
- **Extension Functions**: Create DSL-like APIs for coroutines
- **Structured Concurrency**: Understanding parent-child coroutine relationships

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

## 🏗️ How MathScope Demonstrates Coroutines

### 1. **Custom CoroutineScope Architecture**

```kotlin
open class MathScope : CoroutineScope {
    private val job = Job()
    
    override val coroutineContext: CoroutineContext
        get() = MathScopeConfiguration.uiDispatcher + job
}
```

**What This Does:**
- **Creates a bounded scope** for coroutines related to math operations
- **Manages lifecycle** - when MathScope is destroyed, all its coroutines are cancelled
- **Provides context** - combines a dispatcher (where to run) with a job (lifecycle management)

**Real-World Analogy:** Like a **project manager** who:
- Assigns workers (coroutines) to different departments (dispatchers)
- Can cancel all work when the project ends
- Tracks all ongoing tasks

### 2. **Dispatcher Specialization**

```kotlin
// From MathScopeConfiguration.kt
var uiDispatcher: CoroutineDispatcher = Dispatchers.Main      // UI updates
var backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default  // CPU work
var ioDispatcher: CoroutineDispatcher = Dispatchers.IO        // I/O operations
```

**Real-World Analogy:** Like **specialized work departments**:
- **UI Department**: Handles customer-facing work
- **Computing Department**: Does heavy calculations  
- **I/O Department**: Handles external communications

### 3. **DSL-Like Extension Functions**

Your extension functions create an intuitive API:

```kotlin
// Fire-and-forget operations
fun CoroutineScope.uiJob(block: suspend CoroutineScope.() -> Unit)
fun CoroutineScope.backgroundJob(block: suspend CoroutineScope.() -> Unit)
fun CoroutineScope.ioJob(block: suspend CoroutineScope.() -> Unit)

// Operations that return values
suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T
suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T

// Async operations with Deferred
fun <T> CoroutineScope.backgroundTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T>
```

## 📝 Usage Examples

### Example 1: Basic Calculator Service

```kotlin
class Calculator : MathScope() {
    
    fun performCalculation() {
        // UI update - runs on main thread
        uiJob {
            println("Starting calculation...")
        }
        
        // Heavy computation - runs on background threads
        backgroundJob {
            val result = complexMathOperation()
            println("Calculation complete: $result")
        }
        
        // File operations - runs on I/O threads  
        ioJob {
            saveResultToFile(result)
        }
    }
    
    private suspend fun complexMathOperation(): Double {
        // Simulate heavy computation
        delay(2000) // Suspends coroutine, doesn't block thread
        return 42.0 * 1.5
    }
}
```

### Example 2: Task with Return Value

```kotlin
suspend fun calculateAsync(): Double {
    // This returns a value after completion
    return backgroundTask {
        var sum = 0.0
        repeat(1000000) {
            sum += kotlin.math.sqrt(it.toDouble())
        }
        sum
    }
}

// Usage
val result = calculateAsync() // Waits for result but doesn't block thread
```

### Example 3: Parallel Processing

```kotlin
suspend fun parallelCalculations() {
    // Start multiple calculations simultaneously
    val calc1 = backgroundTaskAsync { heavyCalculation1() }
    val calc2 = backgroundTaskAsync { heavyCalculation2() }
    val calc3 = backgroundTaskAsync { heavyCalculation3() }
    
    // Wait for all to complete
    val results = awaitAllOrCancel(calc1, calc2, calc3)
    println("All results: $results")
}
```

### Example 4: Smart Thread Switching

```kotlin
class SmartCalculator : MathScope() {
    
    suspend fun smartCalculation() {
        // Switch to background for heavy work
        val result = backgroundTask {
            performComplexMath()
        }
        
        // Switch to UI for display
        uiJob {
            displayResult(result)
        }
        
        // Switch to I/O for persistence
        ioTask {
            persistResult(result)
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

## 🎮 Interactive Mental Model

Think of your **MathScope** as a **video game manager**:

1. **MathScope** = Game Engine
   - Manages all game objects (coroutines)
   - Provides shared resources (dispatchers)
   - Can pause/stop the entire game

2. **Dispatchers** = Different Game Threads
   - **UI Thread**: Updates what player sees
   - **Physics Thread**: Calculates game physics
   - **I/O Thread**: Saves/loads game data

3. **Coroutines** = Game Objects
   - Can be paused/resumed
   - Don't block other objects
   - Communicate through shared state

4. **suspend functions** = Cutscenes
   - Game pauses for the cutscene
   - Other systems keep running
   - Can be skipped (cancelled)

## 🌟 Why This Approach Is Powerful

### 1. **No Thread Blocking**
```kotlin
// Traditional approach - blocks thread
fun oldWay() {
    Thread.sleep(1000) // 😱 Thread is blocked
    updateUI()
}

// Your MathScope approach - suspends coroutine
suspend fun newWay() {
    delay(1000) // 😊 Thread is free for other work
    updateUI()
}
```

### 2. **Easy Thread Switching**
```kotlin
suspend fun smartCalculation() {
    // Switch to background for heavy work
    val result = backgroundTask {
        heavyMathOperation()
    }
    
    // Switch to UI for display
    uiJob {
        displayResult(result)
    }
}
```

### 3. **Scalable**
```kotlin
// Can launch thousands of these without performance issues
repeat(10000) {
    backgroundJob {
        performSmallTask()
    }
}
```

## 💡 Key Takeaways from MathScope

Your `MathScope` demonstrates:

1. **🎯 Proper Architecture**: Custom scope for domain-specific operations
2. **🔄 Lifecycle Management**: Automatic cleanup and cancellation
3. **⚡ Performance**: Non-blocking operations with thread switching
4. **🛡️ Safety**: Proper error handling and resource management
5. **🎨 Clean API**: DSL-like extensions for easy usage
6. **📏 Structured Concurrency**: Parent-child coroutine relationships
7. **🧪 Testing Support**: Test-friendly configuration and utilities
8. **🚀 Production-Ready**: Demonstrates real-world patterns

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
