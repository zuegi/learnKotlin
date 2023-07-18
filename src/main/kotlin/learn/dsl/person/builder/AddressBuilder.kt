package learn.dsl.person.builder

import learn.dsl.person.PersonDsl
import learn.dsl.person.model.Address

@PersonDsl
class AddressBuilder {
    var street: String = ""
    var number: Int = 0
    var city: String = ""

    fun build() : Address = Address(street, number, city)
}
