# Learn Kotlin Project 🚀

A comprehensive Kotlin learning playground demonstrating advanced concepts, patterns, and real-world applications.

## 🏗️ Project Structure

This project is organized into two main areas:

### Spring Boot Application (`ch.zuegi.learnkotlin`)
- Production-ready Spring Boot application
- Reactive web endpoints using WebFlux
- Kotlin integration with Spring Framework

### Learning Modules (`learn.*`)
- **Coroutines & Concurrency** - Custom scopes, dispatchers, and async patterns
- **Domain-Specific Languages (DSL)** - Builder patterns and fluent APIs
- **Actor Model** - Channel-based concurrent programming
- **Kotlin Scripting** - Custom script configurations
- **Tree Structures** - Data modeling and tree operations

## 🎯 Key Learning Areas

| Area | Description | Key Files | README |
|------|-------------|-----------|--------|
| **Coroutines** | Advanced coroutine patterns with custom scopes | `learn/coroutine/*` | [📖 Coroutines README](docs/COROUTINES.md) |
| **DSL Development** | Type-safe builder patterns for fluent APIs | `learn/dsl/*` | [📖 DSL README](docs/DSL.md) |
| **Actor Model** | Channel-based concurrent programming | `learn/actors/*` | [📖 Actors README](docs/ACTORS.md) |
| **Spring Boot** | Reactive web application with Kotlin | `ch/zuegi/learnkotlin/*` | [📖 Spring Boot README](docs/SPRING_BOOT.md) |
| **Scripting** | Custom Kotlin script configurations | `learn/script/*` | [📖 Scripting README](docs/SCRIPTING.md) |

## 🚀 Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- Kotlin 2.2.0

### Running the Application
```bash
# Run Spring Boot application
mvn spring-boot:run

# Run specific learning examples
mvn compile exec:java -Dexec.mainClass="learn.MainKt"
```

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test category
mvn test -Dtest="**/*DslTest"
```

## 🧪 Key Features Demonstrated

### ✅ Production-Ready Patterns
- Custom CoroutineScope implementation
- Proper resource management and cleanup
- Comprehensive error handling
- Type-safe configuration

### ✅ Advanced Kotlin Features
- Extension functions for DSL creation
- Generic type parameters with constraints
- Inline functions for performance
- Sealed classes for type safety

### ✅ Concurrent Programming
- Channel-based communication
- Custom thread pool management
- Timeout handling strategies
- Cancellation support

### ✅ Modern Testing
- JUnit 5 integration
- AssertJ for fluent assertions
- Coroutine testing utilities
- Comprehensive test coverage

## 📚 Learning Path

1. **Start with** [Coroutines](docs/COROUTINES.md) - Foundation for async programming
2. **Explore** [DSL Development](docs/DSL.md) - Create fluent, readable APIs
3. **Understand** [Actor Model](docs/ACTORS.md) - Concurrent programming patterns
4. **Integrate** [Spring Boot](docs/SPRING_BOOT.md) - Real-world web applications
5. **Extend** [Scripting](docs/SCRIPTING.md) - Custom language extensions

## 🛠️ Technology Stack

- **Language**: Kotlin 2.2.0
- **Platform**: JVM (Java 21)
- **Framework**: Spring Boot 3.1.1 with WebFlux
- **Concurrency**: Kotlin Coroutines
- **Serialization**: kotlinx.serialization
- **Testing**: JUnit 5, AssertJ, Mockito
- **Build Tool**: Maven

## 🎯 Next Steps

- [ ] Implement Kotlin Flow for reactive streams
- [ ] Add Kotlin Multiplatform modules
- [ ] Integrate Arrow-kt for functional programming
- [ ] Add performance benchmarks
- [ ] Implement custom Kotlin compiler plugins

## 📖 Additional Resources

- [Kotlin Official Documentation](https://kotlinlang.org/docs/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Spring Boot with Kotlin](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.kotlin)

---

**Happy Learning!** 🎉 Explore each learning area through the dedicated README files linked above.


