package com.example.composeApp.model.common

class Stack<T> {
    private val list: MutableList<T> = mutableListOf()

    fun push(element: T) {
        list.add(element)
    }

    fun pop(): T? = list.removeLastOrNull()

    fun peek(): T? = list.lastOrNull()

    fun isEmpty(): Boolean = list.isEmpty()

    override fun toString(): String {
        return "Stack[$list]"
    }
}