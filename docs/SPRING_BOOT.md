# Spring Boot + Kotlin Integration 🍃

This section demonstrates building reactive web applications with Spring Boot and Kotlin, showcasing modern patterns for web development.

## 📁 File Structure

```
ch/zuegi/learnkotlin/
├── LearnKotlinApplication.kt        # Main Spring Boot application
├── config/
│   └── CustomJobRoute.kt           # Reactive routing configuration
└── feature/
    └── job/
        ├── JobDto.kt               # Data transfer objects
        └── JobHandler.kt           # Request handlers (WebFlux)
```

## 🎯 Learning Objectives

- **Reactive Programming**: WebFlux with coroutines
- **Functional Routing**: Kotlin DSL for route configuration
- **Serialization**: kotlinx.serialization integration
- **Dependency Injection**: Spring's DI with Kotlin
- **Testing**: Testing reactive endpoints

## 🚀 Key Features

### 1. Spring Boot Application (`LearnKotlinApplication.kt`)

```kotlin
@SpringBootApplication
class LearnKotlinApplication

fun main(args: Array<String>) {
    runApplication<LearnKotlinApplication>(*args)
}
```

**Kotlin Advantages:**
- **Concise Syntax**: Minimal boilerplate compared to Java
- **Type Safety**: Compile-time null safety
- **Extension Functions**: Enhanced Spring API usability
- **Coroutines**: Native async programming support

### 2. Reactive Routing (`CustomJobRoute.kt`)

```kotlin
@Configuration
class CustomJobRoute(
    private val jobHandler: JobHandler,
) {
    @FlowPreview
    @Bean
    fun jobRoute() = coRouter {
        GET("/api/job/list", jobHandler::listJobs)
        POST("/api/job/add", jobHandler::addJob)
    }
}
```

**Key Concepts:**
- **Functional Routing**: Route configuration as code
- **Constructor Injection**: Kotlin primary constructor DI
- **Coroutine Router**: `coRouter` for suspend function support
- **Method References**: Clean handler binding

### 3. Data Transfer Objects (`JobDto.kt`)

```kotlin
@Serializable
data class JobDto(
    val name: String,
    val taskDto: CustomTaskDto,
)

@Serializable
data class CustomTaskDto(
    val name: String,
)
```

**Benefits:**
- **Immutable by Default**: `data class` creates immutable objects
- **Kotlinx Serialization**: JSON serialization without reflection
- **Type Safety**: Compile-time validation of data structure
- **Automatic Methods**: `equals()`, `hashCode()`, `toString()`, `copy()`

## 🛠️ Configuration & Setup

### Maven Dependencies (Key Kotlin-Specific)

```xml
<dependencies>
    <!-- Spring Boot WebFlux for reactive programming -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    
    <!-- Kotlin standard library -->
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib-jdk8</artifactId>
    </dependency>
    
    <!-- Kotlin reflection for Spring -->
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-reflect</artifactId>
    </dependency>
    
    <!-- Coroutines for reactive integration -->
    <dependency>
        <groupId>org.jetbrains.kotlinx</groupId>
        <artifactId>kotlinx-coroutines-reactor</artifactId>
    </dependency>
    
    <!-- Kotlinx serialization -->
    <dependency>
        <groupId>org.jetbrains.kotlinx</groupId>
        <artifactId>kotlinx-serialization-json</artifactId>
    </dependency>
</dependencies>
```

### Kotlin Maven Plugin Configuration

```xml
<plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <configuration>
        <args>
            <arg>-Xjsr305=strict</arg>
        </args>
        <compilerPlugins>
            <plugin>spring</plugin>
            <plugin>kotlinx-serialization</plugin>
        </compilerPlugins>
        <jvmTarget>17</jvmTarget>
    </configuration>
</plugin>
```

**Key Features:**
- **Spring Plugin**: Enables open classes for Spring proxying
- **Serialization Plugin**: Enables kotlinx.serialization
- **JSR-305 Strict**: Better null-safety integration
- **JVM Target 17**: Modern Java features

## 📝 Implementation Examples

### Reactive Handler Implementation

```kotlin
@Component
class JobHandler {
    
    suspend fun listJobs(request: ServerRequest): ServerResponse {
        // Simulate async data fetch
        val jobs = fetchJobsAsync()
        
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(jobs)
    }
    
    suspend fun addJob(request: ServerRequest): ServerResponse {
        val jobDto = request.awaitBody<JobDto>()
        
        // Validate and process
        val savedJob = saveJobAsync(jobDto)
        
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(savedJob)
    }
    
    private suspend fun fetchJobsAsync(): List<JobDto> = withContext(Dispatchers.IO) {
        // Simulate database call
        delay(100)
        listOf(
            JobDto("Data Processing", CustomTaskDto("ETL")),
            JobDto("Report Generation", CustomTaskDto("PDF"))
        )
    }
    
    private suspend fun saveJobAsync(job: JobDto): JobDto = withContext(Dispatchers.IO) {
        // Simulate save operation
        delay(50)
        job.copy() // Return saved job
    }
}
```

### Advanced Routing Patterns

```kotlin
@Configuration
class AdvancedRoutingConfig {
    
    @Bean
    fun apiRoutes(
        jobHandler: JobHandler,
        userHandler: UserHandler
    ) = coRouter {
        
        "/api".nest {
            
            // Job routes
            "/jobs".nest {
                GET("", jobHandler::listJobs)
                POST("", jobHandler::addJob)
                GET("/{id}", jobHandler::getJob)
                PUT("/{id}", jobHandler::updateJob)
                DELETE("/{id}", jobHandler::deleteJob)
            }
            
            // User routes with filters
            "/users".nest {
                filter(authenticationFilter())
                GET("", userHandler::listUsers)
                POST("", userHandler::createUser)
            }
            
            // Health check
            GET("/health") { 
                ServerResponse.ok().bodyValueAndAwait(mapOf("status" to "UP"))
            }
        }
        
        // Static resources
        resources("/**", ClassPathResource("static/"))
    }
    
    private fun authenticationFilter(): HandlerFilterFunction<ServerResponse, ServerResponse> {
        return HandlerFilterFunction { request, next ->
            // Authentication logic
            val token = request.headers().firstHeader("Authorization")
            if (isValidToken(token)) {
                next.handle(request)
            } else {
                ServerResponse.status(HttpStatus.UNAUTHORIZED).buildAndAwait()
            }
        }
    }
}
```

## 🧪 Testing Reactive Endpoints

### Test Configuration

```kotlin
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["spring.profiles.active=test"])
class JobControllerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient
    
    @Test
    fun `should list jobs`() {
        webTestClient
            .get()
            .uri("/api/job/list")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList<JobDto>()
            .hasSize(2)
            .contains(JobDto("Data Processing", CustomTaskDto("ETL")))
    }
    
    @Test
    fun `should add new job`() {
        val newJob = JobDto("New Job", CustomTaskDto("New Task"))
        
        webTestClient
            .post()
            .uri("/api/job/add")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newJob)
            .exchange()
            .expectStatus().isOk
            .expectBody<JobDto>()
            .isEqualTo(newJob)
    }
}
```

### Unit Testing Handlers

```kotlin
@ExtendWith(MockitoExtension::class)
class JobHandlerTest {
    
    @Mock
    private lateinit var jobService: JobService
    
    @InjectMocks
    private lateinit var jobHandler: JobHandler
    
    @Test
    fun `should handle list jobs request`() = runTest {
        // Given
        val expectedJobs = listOf(
            JobDto("Job 1", CustomTaskDto("Task 1"))
        )
        whenever(jobService.findAll()).thenReturn(expectedJobs)
        
        val request = MockServerRequest.builder()
            .method(HttpMethod.GET)
            .uri("/api/job/list")
            .build()
        
        // When
        val response = jobHandler.listJobs(request)
        
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK)
        
        // Verify response body
        val jobs = response.bodyValue as List<JobDto>
        assertThat(jobs).hasSize(1)
        assertThat(jobs[0].name).isEqualTo("Job 1")
    }
}
```

## 🎯 Advanced Patterns

### 1. Error Handling

```kotlin
@Component
class GlobalErrorHandler : ErrorWebExceptionHandler {
    
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        
        when (ex) {
            is ValidationException -> {
                response.statusCode = HttpStatus.BAD_REQUEST
                return writeErrorResponse(response, "Validation failed: ${ex.message}")
            }
            
            is NotFoundException -> {
                response.statusCode = HttpStatus.NOT_FOUND  
                return writeErrorResponse(response, "Resource not found")
            }
            
            else -> {
                response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                return writeErrorResponse(response, "Internal server error")
            }
        }
    }
    
    private fun writeErrorResponse(
        response: ServerHttpResponse, 
        message: String
    ): Mono<Void> {
        val error = mapOf("error" to message, "timestamp" to Instant.now())
        val buffer = response.bufferFactory().wrap(
            Json.encodeToString(error).toByteArray()
        )
        return response.writeWith(Mono.just(buffer))
    }
}
```

### 2. Custom Serialization

```kotlin
@Serializable
data class JobResponse(
    val id: String,
    val name: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    val status: JobStatus
)

@Serializable
enum class JobStatus {
    PENDING, RUNNING, COMPLETED, FAILED
}

object InstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)
    
    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }
    
    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}
```

### 3. Reactive Data Processing

```kotlin
@Service
class ReactiveJobService {
    
    suspend fun processJobsInBatches(batchSize: Int = 10): Flow<JobResult> = flow {
        val jobs = fetchAllJobs()
        
        jobs.chunked(batchSize).forEach { batch ->
            val results = batch.map { job ->
                async { processJob(job) }
            }.awaitAll()
            
            results.forEach { result ->
                emit(result)
            }
        }
    }
    
    private suspend fun processJob(job: JobDto): JobResult = withContext(Dispatchers.Default) {
        // CPU-intensive processing
        val result = heavyComputation(job)
        JobResult(job.name, result, Instant.now())
    }
}

@RestController
class StreamingController(private val jobService: ReactiveJobService) {
    
    @GetMapping("/api/jobs/process-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun processJobStream(): Flow<JobResult> {
        return jobService.processJobsInBatches()
    }
}
```

### 4. WebSocket Integration

```kotlin
@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(JobProgressHandler(), "/ws/job-progress")
            .setAllowedOrigins("*")
    }
}

@Component
class JobProgressHandler : WebSocketHandler {
    
    override fun afterConnectionEstablished(session: WebSocketSession) {
        // Send job updates to client
        GlobalScope.launch {
            jobProgressFlow().collect { progress ->
                session.sendMessage(TextMessage(Json.encodeToString(progress)))
            }
        }
    }
    
    private fun jobProgressFlow(): Flow<JobProgress> = flow {
        // Emit job progress updates
        while (true) {
            val progress = getCurrentJobProgress()
            emit(progress)
            delay(1000)
        }
    }
}
```

## 🚨 Best Practices

### 1. Null Safety

```kotlin
// Use nullable types appropriately
data class UserDto(
    val name: String,                    // Required field
    val email: String?,                  // Optional field
    val age: Int = 0                     // Default value
)

// Safe handling in handlers
suspend fun getUserHandler(request: ServerRequest): ServerResponse {
    val userId = request.pathVariable("id")
    val user = userService.findById(userId) // Returns User?
    
    return if (user != null) {
        ServerResponse.ok().bodyValueAndAwait(user)
    } else {
        ServerResponse.notFound().buildAndAwait()
    }
}
```

### 2. Extension Functions

```kotlin
// Extend Spring APIs for better usability
suspend fun ServerRequest.extractJobDto(): JobDto {
    return this.awaitBody<JobDto>().also { dto ->
        require(dto.name.isNotBlank()) { "Job name cannot be blank" }
    }
}

suspend fun ServerResponse.Builder.jsonOk(body: Any): ServerResponse {
    return this.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValueAndAwait(body)
}

// Usage
suspend fun addJob(request: ServerRequest): ServerResponse {
    val job = request.extractJobDto()
    val saved = jobService.save(job)
    return ServerResponse.Builder().jsonOk(saved)
}
```

### 3. Configuration Properties

```kotlin
@ConfigurationProperties(prefix = "app.job")
@ConstructorBinding
data class JobProperties(
    val batchSize: Int = 100,
    val timeout: Duration = Duration.ofMinutes(5),
    val retryAttempts: Int = 3,
    val enableMetrics: Boolean = true
)

@Configuration
@EnableConfigurationProperties(JobProperties::class)
class JobConfig(private val properties: JobProperties) {
    
    @Bean
    fun jobExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = properties.batchSize / 10
        executor.maxPoolSize = properties.batchSize
        executor.setThreadNamePrefix("job-")
        executor.initialize()
        return executor
    }
}
```

## 📊 Monitoring & Observability

### Metrics with Micrometer

```kotlin
@Component
class JobMetrics(private val meterRegistry: MeterRegistry) {
    
    private val jobCounter = Counter.builder("jobs.processed")
        .description("Number of jobs processed")
        .register(meterRegistry)
    
    private val processingTimer = Timer.builder("jobs.processing.time")
        .description("Job processing time")
        .register(meterRegistry)
    
    fun recordJobProcessed() {
        jobCounter.increment()
    }
    
    fun <T> timeProcessing(block: suspend () -> T): T {
        return processingTimer.recordSuspendCallable(block)
    }
}
```

### Health Indicators

```kotlin
@Component
class JobHealthIndicator : HealthIndicator {
    
    override fun health(): Health {
        val jobQueueSize = getJobQueueSize()
        val isHealthy = jobQueueSize < 1000
        
        val builder = if (isHealthy) Health.up() else Health.down()
        
        return builder
            .withDetail("queueSize", jobQueueSize)
            .withDetail("maxQueueSize", 1000)
            .build()
    }
}
```

## 🔗 Related Files

- **Tests**: `src/test/kotlin/ch/zuegi/learnkotlin/`
- **Coroutines**: `learn/coroutine/` (async patterns)
- **DSL**: `learn/dsl/` (configuration DSLs)

This Spring Boot integration demonstrates modern web application development with Kotlin, showcasing reactive programming, functional routing, and production-ready patterns.
