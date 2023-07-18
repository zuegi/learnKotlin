package learn.dsl.person.builder

import learn.dsl.person.PersonDsl
import learn.dsl.person.model.Address
import learn.dsl.person.model.Person
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@PersonDsl
class PersonBuilder {

    var name: String = ""

    private var dob: LocalDate = LocalDate.now()
    var dateOfBirth: String = ""
        set(value) {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            dob = LocalDate.parse(value, formatter)
        }

    private val addresses = mutableListOf<Address>()


    fun addresses(block: AddressList.() -> Unit) {
        addresses.addAll(AddressList().apply(block))
    }

    fun build(): Person = Person(name, dob, addresses)

}
