package learn.dsl.person

import learn.dsl.person.model.Person
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PersonDslKtTest {
    @Test
    fun `should create a valid person`() {
        val person =
            person {
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
        assertThat(person.addresses).isNotEmpty.hasSize(1)
        assertThat(person.howOldAmI()).isEqualTo(56)
    }
}
