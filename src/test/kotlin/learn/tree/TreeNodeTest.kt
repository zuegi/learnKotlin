package ch.raiffeisen.financialdatacenter.runtime.node

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TreeNodeTest {
    lateinit var rootNode: TreeNode

    @BeforeEach
    fun rootNode() {
        rootNode = rootNode("root") {}
    }

    @AfterEach
    fun prettyPrint() {
        println(rootNode.prettyString())
    }

    @Test
    fun `Erstelle einen Beispiel NodeTree`() {
        rootNode
            .node("FX Instrument") {
                node("FMD0") {
                    node("CHFEUR", true) { }
                    node("CHFUSD", true) { }
                }
                node("FMD1") { }
            }
        rootNode.node("Noten Instrumente") {
            node("FRN1") { }
        }
    }

    @Test
    fun `Erstelle einen NodeTree mit Berechnungsdefinitionen`() {
        rootNode
            .node("FX Instrument") {
                berechnungsdefinition("FX Berechnungsdefinition") { }
                node("FMD0") {
                    berechnungsdefinition("FMD0 Berechnungsdefinition") { }
                    node("CHFEUR", true) { }
                    node("CHFUSD", true) { }
                }
                node("FMD1") { }
            }
        rootNode.node("Noten Instrumente") {
            node("FRN1") { }
        }
    }

    @Test
    fun `Erstelle einen NodeTree mit Berechnungsdefinitionen und Attributen`() {
        rootNode
            .node("FX Instrument") {
                berechnungsdefinition("FX Berechnungsdefinition") {
                    attributedefinition("FX Attributedefinition") {
                        attribute("4augen") { }
                        attribute("Start Oeffnungstzeit") { }
                        attribute("Ende Oeffnungstzeit") { }
                    }
                }
                node("FMD0") {
                    berechnungsdefinition("FMD0 Berechnungsdefinition") {
                        attributedefinition("FX Attributedefinition") {
                            attribute("Bid") { }
                            attribute("Ask") { }
                            attribute("Mid") { }
                        }
                    }
                    node("CHFEUR", true) { }
                    node("CHFUSD", true) { }
                }
                node("FMD1") { }
            }
        rootNode.node("Noten Instrumente") {
            node("FRN1") { }
        }
    }

    @Test
    fun `Erstelle einen NodeTree mit Attributen und entsprechendem Code`() {
        rootNode
            .node("FX Instrument") {
                berechnungsdefinition("FX Berechnungsdefinition") {
                    codedefinition("FX Code") { }
                    attributedefinition("FX Attributedefinition") {
                        attribute("4augen") { }
                        attribute("Start Oeffnungstzeit") { }
                        attribute("Ende Oeffnungstzeit") { }
                    }
                }
                node("FMD0") {
                    berechnungsdefinition("FMD0 Berechnungsdefinition") {
                        codedefinition("FMD0 Code") { }
                        attributedefinition("FX Attributedefinition") {
                            attribute("Bid") { }
                            attribute("Ask") { }
                            attribute("Mid") { }
                        }
                    }
                    node("CHFEUR", true) { }
                    node("CHFUSD", true) { }
                }
                node("FMD1") { }
            }
        rootNode.node("Noten Instrumente") {
            node("FRN1") { }
        }
    }

    @Test
    fun `Erstelle Berechnungsdefinition mit spezifischem Code an einen TreeNode`() {
        val rootCodefinition =
            rootCodedfinition("FX Code") {
                section("FMD0 Code") { }
            }
        val rootBerechnungsdefinition =
            rootBerechnungsdefinition("FX Berechnungsdefinition") {
                section("FMD0 Berechnungsdefinition") { }
            }

        rootNode
            .node("FX Instrument") {
                berechnungsdefinition(rootBerechnungsdefinition) {
                    codedefinition(rootCodefinition) {}
                }
                node("FMD0") {
                    berechnungsdefinition(rootBerechnungsdefinition.children.get(0)) {
                    }
                    node("CHFEUR", true) { }
                    node("CHFUSD", true) { }
                }
                node("FMD1") { }
            }
        rootNode.node("Noten Instrumente") {
            node("FRN1") { }
        }
    }
}
