# Kotlin Actor Model & Channel-Based Concurrency 🎭

This section demonstrates the Actor Model implementation using Kotlin coroutines and channels for building concurrent, message-passing systems.

## 📁 File Structure

```
learn/actors/
├── Main.kt                          # Demo application
├── actor/
│   ├── SourceActor.kt              # Data producer actor
│   ├── CalclulateActor.kt          # Data processing actor
│   └── SinkActuator.kt             # Data consumer actor
└── model/
    └── Model.kt                    # Shared data models and utilities
```

## 🎯 Learning Objectives

- **Actor Model Pattern**: Message-passing concurrency architecture
- **Channel Communication**: Type-safe communication between actors
- **Pipeline Processing**: Chain actors for data transformation
- **Resource Management**: Proper cleanup and thread pool management
- **Error Handling**: Resilient actor system design

## 🚀 Key Concepts

### What is the Actor Model?

The Actor Model is a conceptual model for concurrent computation where:
- **Actors** are independent computational entities
- **Messages** are the only way actors communicate
- **No Shared State** - actors maintain private state
- **Asynchronous Processing** - actors process messages independently

### Benefits
- **Isolation**: No shared mutable state eliminates race conditions
- **Scalability**: Easy to distribute across multiple threads/processes  
- **Fault Tolerance**: Actor failures don't affect other actors
- **Composability**: Actors can be easily chained and combined

## 🏗️ Architecture Overview

```
[SourceActor] → [CalculateActor] → [SinkActor]
     │               │               │
   Produces        Processes       Consumes
   RawData         RawData→        RichData
                   RichData
```

### Data Flow
1. **SourceActor** generates `RawData` messages
2. **CalculateActor** transforms `RawData` into `RichData`
3. **SinkActor** consumes and processes final `RichData`

## 📊 Data Models

### Core Message Types

```kotlin
data class RawData(val bid: Double, val ask: Double)

data class RichData(val bid: Double, val ask: Double, val mid: Double)

data class ActorsMetaData(
    val timestampMillis: Long, 
    val correlationId: UUID = UUID.randomUUID()
)

data class ActorsMessage<T>(val payload: T, val metaData: ActorsMetaData)
```

### Message Transformation

```kotlin
fun <T, R> transformMessage(
    input: ActorsMessage<T>, 
    block: (T) -> R
): ActorsMessage<R> {
    val result = block(input.payload)
    return ActorsMessage(result, input.metaData)
}
```

**Benefits:**
- Preserves message metadata across transformations
- Maintains correlation IDs for tracing
- Type-safe message transformation

## 🎬 Actor Implementations

### 1. Source Actor (`SourceActor.kt`)

**Purpose**: Generates and publishes data messages

```kotlin
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun CoroutineScope.sourceActor(total: Int) = produce<ActorsMessage<RawData>>(
    context,
    capacity = 10,
    onCompletion = {
        context.close() // Clean up thread pool
        log("🛑 Completed. Exception: $it")
    }
) {
    for (i in 1..total) {
        val rawData = RawData(randomDouble(), randomDouble())
        val result = ActorsMessage(rawData, ActorsMetaData(Instant.now().toEpochMilli()))
        log("🐥Producing $result")
        channel.send(result)
    }
    channel.close()
}
```

**Key Features:**
- **Custom Thread Pool**: Dedicated executor for production
- **Capacity Management**: Bounded channel with backpressure
- **Resource Cleanup**: Automatic context cleanup on completion
- **Logging**: Detailed logging with emojis for easy identification

### 2. Calculate Actor (`CalclulateActor.kt`)

**Purpose**: Processes incoming messages and transforms data

```kotlin
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun CoroutineScope.calculateActor(
    inbox: ReceiveChannel<ActorsMessage<RawData>>
): ReceiveChannel<ActorsMessage<RichData>> = produce(
    context,
    capacity = 100,
    onCompletion = {
        context.close()
        log("🛑 Completed. Exception: $it")
    },
) {
    for (msg in inbox) { // Process each incoming message
        log("🥁 Processing $msg")
        val result = transformMessage(msg) { calculate(msg.payload) }
        log("🥁 Calculated $result")
        channel.send(result)
    }
}

private fun calculate(rawData: RawData): RichData {
    val bid = rawData.bid
    val ask = rawData.ask
    val mid = (bid + ask) / 2
    return RichData(bid, ask, mid)
}
```

**Key Features:**
- **Pipeline Integration**: Takes input channel, produces output channel
- **Message Transformation**: Uses generic transformation function
- **Business Logic Separation**: Calculation logic isolated in private function
- **High Throughput**: Larger capacity for processing-intensive operations

### 3. Sink Actor (`SinkActuator.kt`)

**Purpose**: Final consumer that processes completed data

```kotlin
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun CoroutineScope.sinkActor(
    inbox: ReceiveChannel<ActorsMessage<RichData>>
) {
    launch(context) {
        for (msg in inbox) {
            log("🏁 Consuming $msg")
            // Process final data (save to database, send notifications, etc.)
        }
        context.close()
    }
}
```

**Key Features:**
- **Terminal Actor**: End of the processing pipeline
- **Simple Processing**: Focus on consumption rather than transformation
- **Integration Point**: Where data leaves the actor system

## 🔧 Utilities & Infrastructure

### Thread Management

```kotlin
class NamedThreadFactory(private val prefix: String) : ThreadFactory {
    private val counter = AtomicInteger()
    
    override fun newThread(r: Runnable): Thread {
        val t = Thread(r)
        t.name = "${this.prefix}-${counter.getAndIncrement()}"
        return t
    }
}

private val context = Executors.newFixedThreadPool(5, NamedThreadFactory("producer"))
    .asCoroutineDispatcher()
```

**Benefits:**
- **Named Threads**: Easy identification during debugging
- **Resource Control**: Fixed thread pool prevents resource exhaustion
- **Coroutine Integration**: Seamless integration with coroutine dispatchers

### Logging

```kotlin
fun log(msg: String) = println("${Instant.now()} [${Thread.currentThread().name}] $msg")
```

**Features:**
- **Timestamp**: Track message processing times
- **Thread Information**: See which thread processed each message
- **Emoji Indicators**: Visual differentiation of actor types

## 📝 Usage Example

### Complete Pipeline

```kotlin
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
fun main() = runBlocking {
    
    // Create source actor
    val sourceChannel = sourceActor(total = 100)
    
    // Create processing actor
    val calculateChannel = calculateActor(sourceChannel)
    
    // Create sink actor
    sinkActor(calculateChannel)
    
    // Wait for completion
    delay(5000)
}
```

### Output Example

```
2023-12-07T10:30:15.123 [producer-0] 🐥Producing ActorsMessage(payload=RawData(bid=1.234, ask=1.567), metaData=...)
2023-12-07T10:30:15.124 [calculator-0] 🥁 Processing ActorsMessage(payload=RawData(bid=1.234, ask=1.567), metaData=...)
2023-12-07T10:30:15.125 [calculator-0] 🥁 Calculated ActorsMessage(payload=RichData(bid=1.234, ask=1.567, mid=1.4005), metaData=...)
2023-12-07T10:30:15.126 [main] 🏁 Consuming ActorsMessage(payload=RichData(bid=1.234, ask=1.567, mid=1.4005), metaData=...)
```

## 🧪 Testing Actor Systems

### Test Setup

```kotlin
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ActorSystemTest {
    
    @Test
    fun `should process messages through pipeline`() = runTest {
        val testData = listOf(
            RawData(1.0, 2.0),
            RawData(3.0, 4.0)
        )
        
        // Create test channels
        val sourceChannel = produceTestData(testData)
        val calculateChannel = calculateActor(sourceChannel)
        
        // Collect results
        val results = mutableListOf<ActorsMessage<RichData>>()
        for (msg in calculateChannel) {
            results.add(msg)
        }
        
        // Verify transformations
        assertEquals(2, results.size)
        assertEquals(1.5, results[0].payload.mid) // (1.0 + 2.0) / 2
        assertEquals(3.5, results[1].payload.mid) // (3.0 + 4.0) / 2
    }
}
```

### Mock Actors

```kotlin
@ExperimentalCoroutinesApi
fun CoroutineScope.produceTestData(
    data: List<RawData>
) = produce<ActorsMessage<RawData>> {
    data.forEach { rawData ->
        val message = ActorsMessage(rawData, ActorsMetaData(System.currentTimeMillis()))
        send(message)
    }
}
```

## 🎯 Advanced Patterns

### 1. Error Handling Actor

```kotlin
@ExperimentalCoroutinesApi
fun CoroutineScope.errorHandlingActor(
    inbox: ReceiveChannel<ActorsMessage<RawData>>
): ReceiveChannel<ActorsMessage<RichData>> = produce {
    for (msg in inbox) {
        try {
            val result = transformMessage(msg) { calculate(it) }
            send(result)
        } catch (e: Exception) {
            log("❌ Error processing $msg: ${e.message}")
            // Send to dead letter queue or retry logic
        }
    }
}
```

### 2. Fan-Out Pattern

```kotlin
@ExperimentalCoroutinesApi
fun CoroutineScope.fanOut(
    source: ReceiveChannel<ActorsMessage<RawData>>,
    workerCount: Int = 3
): List<ReceiveChannel<ActorsMessage<RichData>>> {
    return (1..workerCount).map { workerId ->
        produce {
            for (msg in source) {
                log("👷‍♂️ Worker $workerId processing $msg")
                val result = transformMessage(msg) { calculate(it) }
                send(result)
            }
        }
    }
}
```

### 3. Fan-In Pattern

```kotlin
@ExperimentalCoroutinesApi
fun CoroutineScope.fanIn(
    inputs: List<ReceiveChannel<ActorsMessage<RichData>>>
): ReceiveChannel<ActorsMessage<RichData>> = produce {
    inputs.forEach { input ->
        launch {
            for (msg in input) {
                send(msg)
            }
        }
    }
}
```

## 📊 Performance Considerations

### Channel Capacity

```kotlin
// Low capacity - backpressure for flow control
produce(capacity = 10) { /* ... */ }

// High capacity - better throughput, more memory usage
produce(capacity = 1000) { /* ... */ }

// Unlimited - no backpressure (use carefully)
produce(capacity = Channel.UNLIMITED) { /* ... */ }
```

### Thread Pool Sizing

```kotlin
// CPU-intensive work
val cpuContext = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors(),
    NamedThreadFactory("cpu-worker")
).asCoroutineDispatcher()

// I/O-intensive work
val ioContext = Executors.newFixedThreadPool(
    50, // Higher count for I/O operations
    NamedThreadFactory("io-worker")
).asCoroutineDispatcher()
```

## 🚨 Best Practices

1. **Always Clean Up Resources**: Use `onCompletion` to close thread pools
2. **Use Appropriate Capacity**: Balance memory usage and throughput
3. **Named Thread Factories**: Make debugging easier
4. **Preserve Message Metadata**: Use `transformMessage` for traceability
5. **Handle Errors Gracefully**: Implement error handling actors
6. **Test with Mock Data**: Create testable actor pipelines
7. **Monitor Performance**: Log timing and throughput metrics

## 🔍 Monitoring & Debugging

### Performance Metrics

```kotlin
class ActorMetrics {
    private val processedCount = AtomicLong(0)
    private val startTime = System.currentTimeMillis()
    
    fun messageProcessed() {
        val count = processedCount.incrementAndGet()
        if (count % 1000 == 0L) {
            val elapsed = System.currentTimeMillis() - startTime
            val rate = count * 1000.0 / elapsed
            log("📊 Processed $count messages at ${"%.2f".format(rate)} msg/sec")
        }
    }
}
```

### Health Checks

```kotlin
class ActorHealthCheck {
    private var lastMessageTime = System.currentTimeMillis()
    
    fun messageReceived() {
        lastMessageTime = System.currentTimeMillis()
    }
    
    fun isHealthy(timeoutMs: Long = 30_000): Boolean {
        return System.currentTimeMillis() - lastMessageTime < timeoutMs
    }
}
```

## 🔗 Related Files

- **Tests**: `src/test/kotlin/learn/actors/`
- **Coroutines**: `learn/coroutine/` (coroutine patterns)
- **Spring Integration**: `ch/zuegi/learnkotlin/` (reactive streams)

This actor system demonstrates how to build scalable, maintainable concurrent systems using Kotlin's coroutines and channels while following actor model principles.
