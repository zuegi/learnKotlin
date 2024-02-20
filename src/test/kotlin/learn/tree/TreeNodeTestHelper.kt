package ch.raiffeisen.financialdatacenter.runtime.node

fun initTree(): TreeNode {
    return rootNode("root") {
        node("Vendor") {
        }
        node("FX Instrumente") {
            node("FMD0") {
            }
            node("FMD1") {}
        }
        node("Buchmetall Instrumente") {
        }
        node("Edelmetall Instrumente") {
        }
    }
}
