# Getting Started


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



# Reference 
* [A small DSL for Android apps development](https://medium.com/kinandcartacreated/kotlin-coroutines-in-android-part-7-65f65f85824d )
* [Easy JSON in Kotlin with a Type-Safe Builder DSL](https://blog.devgenius.io/writing-a-dsl-in-kotlin-42a9029b93a6)
* https://kpavlov.me/blog/building-kotlin-data-pipelines/

### Reference Documentation
  For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.1/maven-plugin/reference/html/#build-image)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#appendix.configuration-metadata.annotation-processor)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.1.1/reference/htmlsingle/#using.devtools)


