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

# Original Project Notes

## Pipeline DSL Concept

```kotlin
pipeline {
    pipe {
        name = "pipe 1"
        task {
            source {}
        }
        task {
            addition {
                
            }
            division {
                
            }
        }
        task {
            ergaenzeStammdaten {}
        }
        task {
            sink {}
        }
    }
    pipe {
        name = "pipe 2"
        
    }
}
```

Jeder Task erhält seine Daten als Input übergeben und gibt als Output für den nächsten Task weiter. Input und Output sind dabei optional.

### Folgende Typen gibt es
* Source - ist ein Datenproduzent und hat demzufolge keinen Input
* Processor - braucht zwingend einen Input und generiert einen Output
* Sink - erhält einen Input generiert aber keinen Output

Source -> Processor -> Sink

## Kombination von Kotlin Scripts und DSL = External DSL?
Kotlin DSLs sind immer interne DSLs, welche in Programm Code verwendbar sind und der Vereinfachung von Domänen Logik dienen können.

Mit der Kombination in Scripts könnte das eine Art externe DSL sein.


> **_NOTE:_** !!!! Kotlin scripting is Experimental. It may be dropped or changed at any time. Use it only for evaluation purposes !!!!

### Maven dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-scripting-common</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-scripting-jvm</artifactId>
    </dependency>
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-scripting-jvm-host</artifactId>
    </dependency>
</dependencies>
```
Und somit resultiert das folgende, über die [ExtDslMain.kt.main](src/main/kotlin/learn/script/ExtDslMain.kt) Routine ausgeführt File
```kotlin
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
println("Caclulated: ${calculation.calculate()}")
```
in
```text
Caclulated: 0.4
```

## Verwendung von DSLs
* Man könnte für das OneApi eine DSL schreiben, welche das Erstellen und so kapselt !!!!!!
* 

## package learn.actors
Building Kotlin data pipelines


## package learn.coroutine
Was ich erreichen möchte:
```kotlin
job {
  task addition {
    summand1 = 0.2
    summand2 = 0.3
  }

  task division {
    divisor = 2
  }
}
```


## package learn.dsl
tbd


# Reference 
* [A small DSL for Android apps development](https://medium.com/kinandcartacreated/kotlin-coroutines-in-android-part-7-65f65f85824d )
* [Easy JSON in Kotlin with a Type-Safe Builder DSL](https://blog.devgenius.io/writing-a-dsl-in-kotlin-42a9029b93a6)
* https://kpavlov.me/blog/building-kotlin-data-pipelines/
* [Combining scripts and DSLs is Kotlin’s most underrated feature](https://scastiel.dev/kotlin-scripts-dsl-underrated-feature)

### Reference Documentation
  For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/#build-image)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#using.devtools)


