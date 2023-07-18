package learn.dsl.person.builder

import learn.dsl.person.PersonDsl
import learn.dsl.person.model.Address


@PersonDsl
class AddressList: ArrayList<Address>() {

    fun address(block: AddressBuilder.() -> Unit) {
        add(AddressBuilder().apply(block).build())
    }
}
