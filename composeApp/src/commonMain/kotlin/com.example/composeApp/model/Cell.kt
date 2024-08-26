package com.example.composeApp.model

import androidx.compose.ui.graphics.Color

data class Cell(
    val row: Int,
    val col: Int,
    val value: Int,
    val isDefault: Boolean = false
) {
    val showValue: String = if (value == -1) "" else value.toString()

    fun cellColor(
        selectedCell: Cell?
    ): Color {
        if (selectedCell == null) return Color.White
        if (this == selectedCell) return Color.Gray
        if (isInSameLine(selectedCell) || isInSameArea(selectedCell)) return Color.LightGray
        return Color.White
    }

    fun isValid(
        cells: Array<Array<Cell>>
    ): Boolean {
        if (value == -1) return true
        return cells.flatten().any { cell ->
            (isInSameArea(cell) || isInSameLine(cell)) && value == cell.value
        }.not()
    }

    fun isValid(
        value: Int,
        cells: Array<Array<Cell>>
    ): Boolean {
        if (value == -1) return true
        return cells.flatten().any { cell ->
            (isInSameArea(cell) || isInSameLine(cell)) && value == cell.value
        }.not()
    }

    private fun isInSameLine(
        other: Cell
    ): Boolean {
        return (other.row == row).xor(other.col == col)
    }

    private fun isInSameArea(
        other: Cell
    ): Boolean {
        return other.row / 3 == row / 3 &&
                other.col / 3 == col / 3 &&
                other != this
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Cell) {
            other.row == row && other.col == col
        }else false
    }
}