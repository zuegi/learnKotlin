package ch.raiffeisen.financialdatacenter.runtime.node

class TreeNode(
    val name: String,
) {
    val children = mutableListOf<TreeNode>()
    var parent: TreeNode? = null
    var berechnungsdefinition: Berechnungsdefinition? = null
    var codedefinition: Codedefinition? = null

    fun node(
        name: String,
        section: Boolean? = false,
        children: TreeNode.() -> Unit,
    ): TreeNode {
        val node = TreeNode(name).apply(children)
        this.children.addLast(node)
        return node
    }

    fun findNodeByName(name: String): TreeNode? {
        val levelOrderTreeIterator = LevelOrderTreeIterator(this)

        while (levelOrderTreeIterator.hasNext()) {
            val nextNode = levelOrderTreeIterator.next()
            if (name.equals(nextNode.name)) {
                return nextNode
            }
        }
        return null
    }

    // FIXME to be removed
    fun berechnungsdefinition(
        name: String,
        children: Berechnungsdefinition.() -> Unit,
    ): TreeNode {
        val bd = Berechnungsdefinition(name).apply(children)
        this.berechnungsdefinition = bd
        return this
    }

    fun berechnungsdefinition(
        berechnungsdefinition: Berechnungsdefinition,
        children: Berechnungsdefinition.() -> Unit,
    ): Berechnungsdefinition {
        this.berechnungsdefinition = berechnungsdefinition.apply(children)
        return berechnungsdefinition
    }

    fun codedefinition(
        name: String,
        children: Codedefinition.() -> Unit,
    ): TreeNode {
        val cd = Codedefinition(name).apply(children)
        this.codedefinition = cd
        return this
    }

    fun prettyString(): String {
        val stringBuilder = StringBuilder()
        print(stringBuilder, "", "")
        return stringBuilder.toString()
    }

    private fun print(
        stringBuilder: StringBuilder,
        prefix: String,
        childrenPrefix: String,
    ) {
        stringBuilder.append(prefix)
        val bdAppend = if (berechnungsdefinition != null) "*-> ${berechnungsdefinition?.name}" else ""
        val codedefinitionAppend = if (codedefinition != null) "<- ${codedefinition?.name}" else ""
        val attributedefinitionAppend = if (berechnungsdefinition?.attributedefinition != null) "-> Attributes:${berechnungsdefinition?.attributedefinition?.attributes?.map { it.name }}" else ""
        stringBuilder.append("$name $bdAppend $codedefinitionAppend $attributedefinitionAppend")
        stringBuilder.append('\n')
        val childIterator = children.iterator()
        while (childIterator.hasNext()) {
            val node = childIterator.next()
            if (childIterator.hasNext()) {
                node.print(stringBuilder, "$childrenPrefix├── ", "$childrenPrefix│   ")
            } else {
                node.print(stringBuilder, "$childrenPrefix└── ", "$childrenPrefix   ")
            }
        }
    }
}

/**
 * https://github.com/AdrianKuta/Tree-Data-Structure/blob/master/src/commonMain/kotlin/com.github.adriankuta/datastructure/tree/iterators/LevelOrderTreeIterator.kt
 *
 * Tree is iterated by using `Level-order Traversal Algorithm"
 * In level-order traversal we iterating nodes level by level,
 * starting from root, and going deeper and deeper in tree.
 * ```
 * E.g.
 *                    1
 *                  / | \
 *                 /  |   \
 *               2    3     4
 *              / \       / | \
 *             5    6    7  8  9
 *            /   / | \
 *           10  11 12 13
 *
 * Output: 1 2 3 4 5 6 7 8 9 10 11 12 13
 * ```
 */
class LevelOrderTreeIterator(root: TreeNode) : Iterator<TreeNode> {
    private val stack = ArrayDeque<TreeNode>()

    init {
        stack.addLast(root)
    }

    override fun hasNext(): Boolean = stack.isNotEmpty()

    override fun next(): TreeNode {
        val node = stack.removeFirst()
        node.children
            .forEach { stack.addLast(it) }
        return node
    }
}

fun rootNode(
    name: String,
    children: TreeNode.() -> Unit,
): TreeNode {
    return TreeNode(name).apply(children)
}
