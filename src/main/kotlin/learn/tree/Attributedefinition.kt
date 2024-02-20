package ch.raiffeisen.financialdatacenter.runtime.node

class Attributedefinition(
    val name: String,
    val section: Boolean? = false,
) {
    val attributes = mutableListOf<Attribute>()
    private var parent: Attributedefinition? = null

    fun attribute(
        name: String,
        attributes: Attributedefinition.() -> Unit,
    ): Attribute {
        val attribute = Attribute(name)
        this.attributes.addLast(attribute)
        return attribute
    }
}
