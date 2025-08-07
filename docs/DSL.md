# Kotlin Domain-Specific Languages (DSL) 🎨

This section demonstrates creating type-safe, fluent APIs using Kotlin's DSL capabilities with builder patterns and @DslMarker annotations.

## 📁 File Structure

```
learn/dsl/
├── calculation/
│   ├── CalculateDsl.kt                    # Main calculation DSL
│   ├── builder/
│   │   ├── CalculationBuilder.kt          # Root builder
│   │   ├── CalcOperationBuilder.kt        # Operation builder
│   │   ├── AdditionBuilder.kt             # Addition operations
│   │   ├── SubtractionBuilder.kt          # Subtraction operations
│   │   ├── MultiplicationBuilder.kt       # Multiplication operations
│   │   └── DivisionBuilder.kt             # Division operations
│   └── model/
│       ├── Calculation.kt                 # Main calculation model
│       └── operation/
│           ├── CalcOperation.kt           # Base operation interface
│           ├── Addition.kt                # Addition implementation
│           ├── Subtraction.kt             # Subtraction implementation
│           ├── Multiplication.kt          # Multiplication implementation
│           └── Division.kt                # Division implementation
└── person/
    ├── PersonDsl.kt                       # Main person DSL
    ├── builder/
    │   ├── PersonBuilder.kt               # Person builder
    │   ├── AddressBuilder.kt              # Address builder
    │   └── AddressList.kt                 # Address collection builder
    └── model/
        ├── Person.kt                      # Person model
        └── Address.kt                     # Address model
```

## 🎯 Learning Objectives

- **Builder Pattern**: Implement fluent builder APIs
- **@DslMarker**: Prevent scope leakage in DSLs
- **Type Safety**: Create compile-time safe DSLs
- **Nested DSLs**: Build complex hierarchical structures
- **Extension Functions**: Enhance DSL readability

## 🚀 Key Features

### 1. Calculation DSL

A mathematical operations DSL that supports chained calculations:

```kotlin
val calculation = calculate {
    operation {
        addition {
            summand1 = 0.2
            summand2 = 0.3
        }
        multiplication {
            factor1 = 2.0
            factor2 = 3.0
        }
    }
}

val result = calculation.calculate() // Returns computed result
```

### 2. Person DSL

A person creation DSL with nested address support:

```kotlin
val person = person {
    name = "René"
    dateOfBirth = "17.06.1969"
    addresses {
        address {
            street = "Lörenstrasse"
            number = 5
            city = "Flawil"
        }
    }
}
```

## 🛠️ DSL Architecture

### Core Components

#### 1. DSL Marker Annotation

```kotlin
@DslMarker
annotation class CalculateDsl

@DslMarker
annotation class PersonDsl
```

**Purpose:**
- Prevents accidentally calling methods from outer scopes
- Ensures type safety in nested DSL blocks
- Provides better IDE support and error messages

#### 2. Entry Point Functions

```kotlin
fun calculate(block: CalculationBuilder.() -> Unit): Calculation = 
    CalculationBuilder().apply(block).build()

fun person(block: PersonBuilder.() -> Unit): Person = 
    PersonBuilder().apply(block).build()
```

**Pattern:**
- Take a lambda with receiver (extension function)
- Apply the lambda to a builder instance
- Return the built object

#### 3. Builder Classes

```kotlin
@CalculateDsl
class CalculationBuilder {
    private var operations = mutableListOf<CalcOperation>()
    
    fun operation(block: CalcOperationBuilder.() -> Unit) {
        val builder = CalcOperationBuilder()
        builder.apply(block)
        operations.addAll(builder.build())
    }
    
    fun build(): Calculation = Calculation(operations)
}
```

## 📝 Detailed Examples

### Calculation DSL Usage

#### Simple Addition
```kotlin
val simple = calculate {
    operation {
        addition {
            summand1 = 0.1
            summand2 = 0.3
        }
    }
}
// Result: 0.4
```

#### Chained Operations
```kotlin
val chained = calculate {
    operation {
        addition {
            summand1 = 0.2
            summand2 = 0.3
        }
        // Previous result becomes factor1
        multiplication {
            factor2 = 2.0
        }
    }
}
// Result: (0.2 + 0.3) * 2.0 = 1.0
```

#### Variable Reuse
```kotlin
val withVariables = calculate {
    operation {
        val sum = addition {
            summand1 = 0.2
            summand2 = 0.3
        }
        multiplication {
            factor1 = sum      // Explicit variable usage
            factor2 = 2.0
        }
    }
}
// Result: 0.5 * 2.0 = 1.0
```

### Person DSL Usage

#### Basic Person
```kotlin
val basicPerson = person {
    name = "John Doe"
    dateOfBirth = "01.01.1990"
}
```

#### Person with Multiple Addresses
```kotlin
val personWithAddresses = person {
    name = "Jane Smith"
    dateOfBirth = "15.03.1985"
    
    addresses {
        address {
            street = "Main Street"
            number = 123
            city = "Springfield"
        }
        
        address {
            street = "Oak Avenue"
            number = 456
            city = "Riverside"
        }
    }
}
```

## 🏗️ Builder Implementation Details

### Calculation Builder Pattern

#### Operation Builder
```kotlin
@CalculateDsl
class CalcOperationBuilder {
    private val operations = mutableListOf<CalcOperation>()
    private var lastResult: Double = 0.0
    
    fun addition(block: AdditionBuilder.() -> Unit): Double {
        val builder = AdditionBuilder()
        builder.apply(block)
        val operation = builder.build()
        operations.add(operation)
        
        lastResult = operation.calculate(lastResult)
        return lastResult
    }
    
    // Similar for multiplication, subtraction, division...
}
```

#### Specific Operation Builders
```kotlin
@CalculateDsl
class AdditionBuilder {
    var summand1: Double = 0.0
    var summand2: Double = 0.0
    
    fun build(): Addition = Addition(summand1, summand2)
}

@CalculateDsl 
class MultiplicationBuilder {
    var factor1: Double = 0.0
    var factor2: Double = 0.0
    
    fun build(): Multiplication = Multiplication(factor1, factor2)
}
```

### Person Builder Pattern

#### Main Person Builder
```kotlin
@PersonDsl
class PersonBuilder {
    var name: String = ""
    var dateOfBirth: String = ""
    private val addressList = AddressList()
    
    fun addresses(block: AddressList.() -> Unit) {
        addressList.apply(block)
    }
    
    fun build(): Person = Person(
        name = name,
        dateOfBirth = dateOfBirth,
        addresses = addressList.addresses
    )
}
```

#### Address Collection Builder
```kotlin
@PersonDsl
class AddressList {
    val addresses = mutableListOf<Address>()
    
    fun address(block: AddressBuilder.() -> Unit) {
        val builder = AddressBuilder()
        builder.apply(block)
        addresses.add(builder.build())
    }
}
```

## 🧪 Testing DSLs

### Calculation DSL Tests

```kotlin
@Test
fun `should calculate addition and then multiply`() {
    val calculation = calculate {
        operation {
            addition {
                summand1 = 0.2
                summand2 = 0.3
            }
            multiplication {
                factor2 = 2.0  // factor1 will be result from addition
            }
        }
    }
    
    assertThat(calculation.calculate()).isEqualTo(1.0)
}

@Test
fun `should handle default values`() {
    val calculation = calculate {
        operation {
            multiplication {
                factor1 = 0.3
                // factor2 defaults to 0.0
            }
        }
    }
    
    assertThat(calculation.calculate()).isEqualTo(0.0)
}
```

### Person DSL Tests

```kotlin
@Test
fun `should create valid person with addresses`() {
    val person = person {
        name = "René"
        dateOfBirth = "17.06.1969"
        addresses {
            address {
                street = "Lörenstrasse"
                number = 5
                city = "Flawil"
            }
        }
    }
    
    assertThat(person).isInstanceOf(Person::class.java)
    assertThat(person.addresses).hasSize(1)
    assertThat(person.howOldAmI()).isEqualTo(56)
}
```

## 🎯 Advanced DSL Patterns

### 1. Conditional DSL Blocks

```kotlin
fun conditionalCalculation(useMultiplication: Boolean) = calculate {
    operation {
        addition {
            summand1 = 1.0
            summand2 = 2.0
        }
        
        if (useMultiplication) {
            multiplication {
                factor2 = 3.0
            }
        }
    }
}
```

### 2. DSL with Validation

```kotlin
@CalculateDsl
class ValidatingAdditionBuilder {
    var summand1: Double = 0.0
        set(value) {
            require(value >= 0) { "Summand must be non-negative" }
            field = value
        }
    
    var summand2: Double = 0.0
        set(value) {
            require(value >= 0) { "Summand must be non-negative" }
            field = value
        }
}
```

### 3. DSL with Default Configurations

```kotlin
fun calculateWithDefaults(
    defaultValue: Double = 1.0,
    block: CalculationBuilder.() -> Unit
): Calculation {
    val builder = CalculationBuilder().apply {
        // Set default operations if needed
        this.defaultValue = defaultValue
    }
    return builder.apply(block).build()
}
```

## 📚 Key Design Principles

### 1. Type Safety
- Use `@DslMarker` to prevent scope leakage
- Leverage Kotlin's type system for compile-time safety
- Provide meaningful error messages

### 2. Readability
- Design DSL to read like natural language
- Use descriptive property and function names
- Minimize boilerplate code

### 3. Flexibility
- Support both explicit and implicit patterns
- Allow variable reuse within DSL blocks
- Provide sensible defaults

### 4. Composability
- Enable nesting of DSL blocks
- Support multiple instances within the same scope
- Allow extension of existing DSLs

## 🚨 Best Practices

### 1. Always Use @DslMarker
```kotlin
@DslMarker
annotation class MyDsl

@MyDsl
class MyBuilder {
    // Builder implementation
}
```

### 2. Provide Clear Entry Points
```kotlin
// Good: Clear, descriptive function name
fun createPerson(block: PersonBuilder.() -> Unit): Person

// Avoid: Generic or unclear names
fun build(block: Builder.() -> Unit): Any
```

### 3. Handle Edge Cases
```kotlin
@MyDsl
class SafeBuilder {
    var value: String = ""
        set(newValue) {
            require(newValue.isNotBlank()) { "Value cannot be blank" }
            field = newValue
        }
}
```

### 4. Document DSL Usage
```kotlin
/**
 * Creates a calculation with the specified operations.
 * 
 * Example:
 * ```kotlin
 * val result = calculate {
 *     operation {
 *         addition {
 *             summand1 = 1.0
 *             summand2 = 2.0
 *         }
 *     }
 * }
 * ```
 */
fun calculate(block: CalculationBuilder.() -> Unit): Calculation
```

## 🔗 Related Files

- **Tests**: `src/test/kotlin/learn/dsl/`
- **Scripting Integration**: `learn/script/` (using DSLs in scripts)
- **Spring Boot**: `ch/zuegi/learnkotlin/` (DSL patterns in configuration)

This DSL framework demonstrates how to create maintainable, type-safe, and readable domain-specific languages in Kotlin using builder patterns and advanced language features.
