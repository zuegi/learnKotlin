package learn.dsl.person

import learn.dsl.person.builder.PersonBuilder
import learn.dsl.person.model.Person

@DslMarker
annotation class PersonDsl

// https://proandroiddev.com/writing-dsls-in-kotlin-part-2-cd9dcd0c4715

// siehe auch learn.dsl.person.PersonDslKtTest
fun person(block: PersonBuilder.() -> Unit): Person = PersonBuilder().apply(block).build()

