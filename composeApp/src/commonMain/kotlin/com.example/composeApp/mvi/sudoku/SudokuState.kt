package com.example.composeApp.mvi.sudoku

import com.example.composeApp.model.Cell
import com.example.composeApp.model.enums.RunState
import com.example.composeApp.mvi.State

data class SudokuState(
    val cells: Array<Array<Cell>> = Array(9) { row ->
        Array(9) { col ->
            Cell(row, col, -1)
        }
    },
    val selectedCell: Cell? = null,
    val runState: RunState = RunState.Init,
): State {
    val isValidSudoku: Boolean = cells.flatten().none {
        it.isValid(cells).not()
    }

    val isResultCleared: Boolean = cells.flatten().any {
        it.isDefault.not() && it.value == -1
    }
}
