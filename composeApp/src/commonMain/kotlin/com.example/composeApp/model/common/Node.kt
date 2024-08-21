package com.example.composeApp.model.common

data class Node<T>(
    var value: T,
    var last: Node<T>? = null,
    var next: Node<T>? = null
) {
    override fun toString(): String {
        return "last(${last?.value}), " +
                "value($value), " +
                "next(${next?.value})"
    }
}

fun <T> List<T>.toNode(): Node<T> {
    if (this.isEmpty()) throw IllegalStateException("Cannot convert empty list to Node<T>")
    val head = Node(
        value = this.first()
    )
    var current: Node<T> = head

    for (i in 1 until this.size) {
        val newNode = Node(this[i])
        current.next = newNode
        newNode.last = current
        current = newNode
    }

    return head
}
