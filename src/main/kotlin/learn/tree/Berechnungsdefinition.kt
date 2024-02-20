package ch.raiffeisen.financialdatacenter.runtime.node

class Berechnungsdefinition(
    val name: String,
    val section: Boolean? = false,
) {
    val children = mutableListOf<Berechnungsdefinition>()
    private var parent: Berechnungsdefinition? = null
    var attributedefinition: Attributedefinition? = null
    var codedefinition: Codedefinition? = null

    fun section(
        name: String,
        children: Berechnungsdefinition.() -> Unit,
    ): Berechnungsdefinition {
        val berechnungsdefinition = Berechnungsdefinition(name, true).apply(children)
        this.children.addLast(berechnungsdefinition)
        return berechnungsdefinition
    }

    fun codedefinition(
        codedefinition: Codedefinition,
        children: Codedefinition.() -> Unit,
    ): Berechnungsdefinition {
        println("hallo")
        this.codedefinition = codedefinition.apply(children)
        return this
    }

    fun attributedefinition(
        name: String,
        children: Attributedefinition.() -> Unit,
    ): Berechnungsdefinition {
        val ad = Attributedefinition(name).apply(children)
        this.attributedefinition = ad
        return this
    }
}

fun rootBerechnungsdefinition(
    name: String,
    children: Berechnungsdefinition.() -> Unit,
): Berechnungsdefinition {
    return Berechnungsdefinition(name).apply(children)
}
