# Kotlin Scripting & External DSLs 📜

This section demonstrates Kotlin's scripting capabilities, showing how to create external DSLs by combining Kotlin scripts with internal DSLs.

## 📁 File Structure

```
learn/script/
├── ExtDslMain.kt           # Script execution engine
├── ExtDslScript.kt         # Script configuration and template
└── ExtDslScriptHost.kt     # Script host implementation
```

## 🎯 Learning Objectives

- **Kotlin Scripting**: Execute Kotlin code dynamically
- **External DSLs**: Combine scripts with internal DSLs
- **Script Configuration**: Custom script compilation settings
- **Dynamic Execution**: Runtime script evaluation
- **Security Considerations**: Safe script execution patterns

## 🚀 Key Concepts

### Internal vs External DSLs

**Internal DSL** (embedded in Kotlin code):
```kotlin
val calculation = calculate {
    operation {
        addition { summand1 = 0.2; summand2 = 0.3 }
    }
}
```

**External DSL** (standalone script file):
```kotlin
// calculation.extdsl.kts
val calculation = calculate {
    operation {
        addition {
            summand1 = 0.2
            summand2 = 0.3
        }
        subtraction {
            subtrahend = 0.1
        }
    }
}
println("Calculated: ${calculation.calculate()}")
```

### Benefits of External DSLs
- **Separation of Concerns**: Business logic separate from application code
- **Non-Technical Users**: Domain experts can write scripts
- **Runtime Flexibility**: Scripts can be modified without recompiling
- **Configuration as Code**: Complex configurations in readable format

## 🛠️ Implementation

### 1. Script Template (`ExtDslScript.kt`)

```kotlin
@KotlinScript(
    fileExtension = "extdsl.kts",
    compilationConfiguration = ExtDslScript.ExtDslScriptConfiguration::class
)
abstract class ExtDslScript {

    object ExtDslScriptConfiguration: ScriptCompilationConfiguration({
        defaultImports(
            "learn.dsl.calculation.calculate"
        )
    })
}
```

**Key Features:**
- **File Extension**: Custom `.extdsl.kts` extension
- **Default Imports**: Automatically import DSL functions
- **Compilation Configuration**: Control script compilation behavior

### 2. Script Host Implementation

```kotlin
class ExtDslScriptHost {
    
    private val scriptEngine by lazy {
        ScriptEngineManager().getEngineByExtension("kts")
            ?: throw IllegalStateException("Kotlin script engine not found")
    }
    
    suspend fun executeScript(scriptContent: String): Any? = withContext(Dispatchers.IO) {
        try {
            scriptEngine.eval(scriptContent)
        } catch (e: ScriptException) {
            throw ScriptExecutionException("Script execution failed", e)
        }
    }
    
    suspend fun executeScriptFromFile(file: File): Any? {
        require(file.exists()) { "Script file does not exist: ${file.path}" }
        require(file.extension == "kts") { "Invalid script file extension" }
        
        return executeScript(file.readText())
    }
}

class ScriptExecutionException(message: String, cause: Throwable) : Exception(message, cause)
```

### 3. Main Execution Engine (`ExtDslMain.kt`)

```kotlin
fun main() = runBlocking {
    val scriptHost = ExtDslScriptHost()
    
    // Example 1: Execute script from string
    val scriptContent = """
        val calculation = calculate {
            operation {
                addition {
                    summand1 = 0.2
                    summand2 = 0.3
                }
                subtraction {
                    subtrahend = 0.1
                }
            }
        }
        println("Calculated: ${'$'}{calculation.calculate()}")
    """.trimIndent()
    
    try {
        scriptHost.executeScript(scriptContent)
    } catch (e: ScriptExecutionException) {
        println("Script execution failed: ${e.message}")
    }
    
    // Example 2: Execute script from file
    val scriptFile = File("scripts/example.extdsl.kts")
    if (scriptFile.exists()) {
        scriptHost.executeScriptFromFile(scriptFile)
    }
}
```

## 📝 Usage Examples

### Simple Calculation Script

```kotlin
// simple.extdsl.kts
val result = calculate {
    operation {
        addition {
            summand1 = 10.0
            summand2 = 5.0
        }
        multiplication {
            factor2 = 2.0
        }
    }
}

println("Simple calculation result: ${result.calculate()}")
// Output: Simple calculation result: 30.0
```

### Complex Business Logic Script

```kotlin
// business-rules.extdsl.kts
fun processOrder(orderValue: Double, customerType: String): Double {
    return calculate {
        operation {
            // Base calculation
            addition {
                summand1 = orderValue
                summand2 = 0.0
            }
            
            // Apply discount based on customer type
            when (customerType) {
                "PREMIUM" -> multiplication { factor2 = 0.9 } // 10% discount
                "VIP" -> multiplication { factor2 = 0.8 }     // 20% discount
                else -> multiplication { factor2 = 1.0 }      // No discount
            }
            
            // Add tax
            multiplication { factor2 = 1.08 } // 8% tax
        }
    }.calculate()
}

// Process different order types
val premiumOrder = processOrder(100.0, "PREMIUM")
val vipOrder = processOrder(100.0, "VIP")
val regularOrder = processOrder(100.0, "REGULAR")

println("Premium order total: $premiumOrder")
println("VIP order total: $vipOrder")
println("Regular order total: $regularOrder")
```

### Configuration Script

```kotlin
// app-config.extdsl.kts
data class DatabaseConfig(
    val host: String,
    val port: Int,
    val database: String,
    val maxConnections: Int
)

data class AppConfig(
    val database: DatabaseConfig,
    val features: List<String>
)

val config = AppConfig(
    database = DatabaseConfig(
        host = "localhost",
        port = 5432,
        database = "production_db",
        maxConnections = 20
    ),
    features = listOf("authentication", "metrics", "caching")
)

// Configuration validation
require(config.database.maxConnections > 0) { 
    "Max connections must be positive" 
}
require(config.features.isNotEmpty()) { 
    "At least one feature must be enabled" 
}

println("Configuration loaded successfully")
config
```

## 🧪 Advanced Script Features

### 1. Script with Parameters

```kotlin
class ParameterizedScriptHost {
    
    suspend fun executeScriptWithParameters(
        scriptContent: String,
        parameters: Map<String, Any>
    ): Any? = withContext(Dispatchers.IO) {
        
        val engine = ScriptEngineManager().getEngineByExtension("kts")
        
        // Inject parameters into script context
        parameters.forEach { (key, value) ->
            engine.put(key, value)
        }
        
        engine.eval(scriptContent)
    }
}

// Usage
val scriptHost = ParameterizedScriptHost()
val parameters = mapOf(
    "baseValue" to 100.0,
    "multiplier" to 1.5
)

val scriptWithParams = """
    val result = calculate {
        operation {
            addition {
                summand1 = baseValue
                summand2 = 0.0
            }
            multiplication {
                factor2 = multiplier
            }
        }
    }
    result.calculate()
""".trimIndent()

val result = scriptHost.executeScriptWithParameters(scriptWithParams, parameters)
```

### 2. Script Validation and Sandboxing

```kotlin
class SecureScriptHost {
    
    private val allowedImports = setOf(
        "learn.dsl.calculation.*",
        "kotlin.math.*"
    )
    
    private val forbiddenPatterns = listOf(
        Regex("import\\s+java\\.io\\..*"),      // No file I/O
        Regex("import\\s+java\\.net\\..*"),     // No network access
        Regex("System\\.exit"),                  // No system exit
        Regex("Runtime\\.getRuntime")           // No runtime access
    )
    
    suspend fun validateAndExecuteScript(scriptContent: String): Any? {
        validateScript(scriptContent)
        return executeScript(scriptContent)
    }
    
    private fun validateScript(scriptContent: String) {
        // Check for forbidden patterns
        forbiddenPatterns.forEach { pattern ->
            if (pattern.containsMatchIn(scriptContent)) {
                throw SecurityException("Script contains forbidden pattern: $pattern")
            }
        }
        
        // Validate imports
        val importPattern = Regex("import\\s+([\\w.]+)")
        importPattern.findAll(scriptContent).forEach { match ->
            val import = match.groupValues[1]
            if (!isImportAllowed(import)) {
                throw SecurityException("Import not allowed: $import")
            }
        }
    }
    
    private fun isImportAllowed(import: String): Boolean {
        return allowedImports.any { allowed ->
            import.matches(Regex(allowed.replace("*", ".*")))
        }
    }
    
    private suspend fun executeScript(scriptContent: String): Any? = withContext(Dispatchers.IO) {
        val engine = ScriptEngineManager().getEngineByExtension("kts")
        
        // Set up security manager or restricted context
        engine.eval(scriptContent)
    }
}
```

### 3. Script Caching and Performance

```kotlin
class CachedScriptHost {
    
    private val scriptCache = ConcurrentHashMap<String, CompiledScript>()
    
    suspend fun executeScriptWithCaching(
        scriptId: String,
        scriptContent: String
    ): Any? = withContext(Dispatchers.IO) {
        
        val compiledScript = scriptCache.computeIfAbsent(scriptId) {
            compileScript(scriptContent)
        }
        
        compiledScript.eval()
    }
    
    private fun compileScript(scriptContent: String): CompiledScript {
        val engine = ScriptEngineManager().getEngineByExtension("kts") as Compilable
        return engine.compile(scriptContent)
    }
    
    fun clearCache() {
        scriptCache.clear()
    }
    
    fun getCacheStats(): Map<String, Any> {
        return mapOf(
            "cacheSize" to scriptCache.size,
            "cachedScripts" to scriptCache.keys.toList()
        )
    }
}
```

## 🧪 Testing Scripts

### Unit Testing Script Execution

```kotlin
class ScriptHostTest {
    
    private val scriptHost = ExtDslScriptHost()
    
    @Test
    fun `should execute simple calculation script`() = runTest {
        val script = """
            val calculation = calculate {
                operation {
                    addition {
                        summand1 = 2.0
                        summand2 = 3.0
                    }
                }
            }
            calculation.calculate()
        """.trimIndent()
        
        val result = scriptHost.executeScript(script) as Double
        assertEquals(5.0, result)
    }
    
    @Test
    fun `should handle script execution errors`() = runTest {
        val invalidScript = """
            val result = someUndefinedFunction()
        """.trimIndent()
        
        assertThrows<ScriptExecutionException> {
            scriptHost.executeScript(invalidScript)
        }
    }
    
    @Test
    fun `should execute script from file`() = runTest {
        val tempFile = createTempFile(suffix = ".kts")
        tempFile.writeText("""
            val calculation = calculate {
                operation {
                    multiplication {
                        factor1 = 4.0
                        factor2 = 5.0
                    }
                }
            }
            calculation.calculate()
        """.trimIndent())
        
        val result = scriptHost.executeScriptFromFile(tempFile) as Double
        assertEquals(20.0, result)
        
        tempFile.delete()
    }
}
```

### Integration Testing

```kotlin
@SpringBootTest
class ScriptIntegrationTest {
    
    @Test
    fun `should integrate with Spring Boot application`() {
        val scriptContent = """
            // This script could interact with Spring beans
            // if we set up the proper script context
            val config = mapOf(
                "database.url" to "jdbc:postgresql://localhost:5432/test",
                "server.port" to 8080
            )
            config
        """.trimIndent()
        
        // Execute script and verify integration
        // Implementation depends on how scripts interact with Spring context
    }
}
```

## 🎯 Real-World Use Cases

### 1. Business Rules Engine

```kotlin
// rules/order-validation.extdsl.kts
fun validateOrder(order: Map<String, Any>): List<String> {
    val errors = mutableListOf<String>()
    
    val amount = order["amount"] as? Double ?: 0.0
    val customerType = order["customerType"] as? String ?: ""
    val items = order["items"] as? List<*> ?: emptyList<Any>()
    
    // Business rule: Minimum order amount
    if (amount < 10.0) {
        errors.add("Minimum order amount is $10")
    }
    
    // Business rule: VIP customers have different limits
    if (customerType == "VIP" && amount > 10000.0) {
        errors.add("VIP orders above $10,000 require manual approval")
    }
    
    // Business rule: Maximum items per order
    if (items.size > 50) {
        errors.add("Maximum 50 items per order")
    }
    
    return errors
}
```

### 2. Data Transformation Pipeline

```kotlin
// transforms/data-pipeline.extdsl.kts
fun transformData(input: Map<String, Any>): Map<String, Any> {
    val transformed = mutableMapOf<String, Any>()
    
    // Extract and transform fields
    transformed["id"] = input["raw_id"]?.toString()?.uppercase() ?: ""
    transformed["amount"] = (input["raw_amount"] as? String)?.toDoubleOrNull() ?: 0.0
    transformed["timestamp"] = System.currentTimeMillis()
    
    // Apply business calculations
    val baseAmount = transformed["amount"] as Double
    val processedAmount = calculate {
        operation {
            addition {
                summand1 = baseAmount
                summand2 = 0.0
            }
            // Apply processing fee (2%)
            multiplication {
                factor2 = 1.02
            }
        }
    }.calculate()
    
    transformed["processed_amount"] = processedAmount
    
    return transformed
}
```

### 3. Configuration Management

```kotlin
// config/environment-setup.extdsl.kts
val environment = System.getenv("ENVIRONMENT") ?: "development"

val databaseConfig = when (environment) {
    "production" -> mapOf(
        "url" to "jdbc:postgresql://prod-db:5432/app",
        "maxConnections" to 100,
        "ssl" to true
    )
    "staging" -> mapOf(
        "url" to "jdbc:postgresql://staging-db:5432/app",
        "maxConnections" to 50,
        "ssl" to true
    )
    else -> mapOf(
        "url" to "jdbc:postgresql://localhost:5432/app_dev",
        "maxConnections" to 10,
        "ssl" to false
    )
}

val appConfig = mapOf(
    "database" to databaseConfig,
    "features" to listOf("metrics", "health-checks"),
    "environment" to environment
)

println("Loaded configuration for environment: $environment")
appConfig
```

## 🚨 Best Practices

### 1. Security Considerations
```kotlin
// Always validate and sanitize script input
// Restrict available APIs and imports
// Use security managers for script execution
// Implement timeout mechanisms
```

### 2. Error Handling
```kotlin
// Provide clear error messages
// Log script execution failures
// Implement fallback mechanisms
// Validate scripts before execution
```

### 3. Performance Optimization
```kotlin
// Cache compiled scripts
// Use appropriate thread pools for execution
// Implement script execution timeouts
// Monitor memory usage
```

### 4. Development Workflow
```kotlin
// Version control script files
// Implement script testing frameworks
// Use CI/CD for script deployment
// Document script APIs and usage
```

## 📊 Monitoring & Debugging

### Script Execution Metrics

```kotlin
class ScriptMetrics(private val meterRegistry: MeterRegistry) {
    
    private val executionCounter = Counter.builder("script.executions")
        .register(meterRegistry)
    
    private val executionTimer = Timer.builder("script.execution.time")
        .register(meterRegistry)
    
    fun recordExecution(scriptId: String, success: Boolean) {
        executionCounter.increment(
            Tags.of(
                Tag.of("script", scriptId),
                Tag.of("success", success.toString())
            )
        )
    }
    
    fun <T> timeExecution(block: () -> T): T {
        return executionTimer.recordCallable(block)
    }
}
```

## 🔗 Related Files

- **DSL Implementation**: `learn/dsl/` (internal DSLs used in scripts)
- **Tests**: `src/test/kotlin/learn/script/`
- **Example Scripts**: `scripts/` directory (if exists)

This scripting framework demonstrates how to extend Kotlin's DSL capabilities to create external configuration and business rule systems while maintaining type safety and performance.
