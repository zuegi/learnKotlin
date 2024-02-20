package ch.raiffeisen.financialdatacenter.runtime.node

class Codedefinition(
    val name: String,
    val section: Boolean? = false,
) {
    val children = mutableListOf<Codedefinition>()
    private var parent: Codedefinition? = null

    fun section(
        name: String,
        children: Codedefinition.() -> Unit,
    ): Codedefinition {
        val codeDefinition = Codedefinition(name, true).apply(children)
        this.children.addLast(codeDefinition)
        return codeDefinition
    }
}

fun rootCodedfinition(
    name: String,
    children: Codedefinition.() -> Unit,
): Codedefinition {
    return Codedefinition(name).apply(children)
}
