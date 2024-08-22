package com.example.composeApp.model

data class Cell(
    val row: Int,
    val col: Int,
    val value: Int,
    val isValid: Boolean,
    val isDefault: Boolean = false
) {
    val showValue: String = if (value == -1) "" else value.toString()

    fun isSameCell(other: Cell): Boolean = other.row == row && other.col == col

    fun isSameRowOrCol(other: Cell): Boolean = (other.row == row).xor(other.col == col)

    fun isSameBox(other: Cell): Boolean =
        (other.row / 3 == row / 3) && (other.col / 3 == col / 3)
}