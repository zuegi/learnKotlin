package learn.dsl.person.model

import java.time.LocalDate
import java.time.Period

data class Person(
    val name: String? = null,
    val dateOfBirth: LocalDate,
    val addresses: List<Address>
)
{
    fun howOldAmI(): Int {
        val now = LocalDate.now()
        return Period.between(
            dateOfBirth, now
        ).years
    }
}

