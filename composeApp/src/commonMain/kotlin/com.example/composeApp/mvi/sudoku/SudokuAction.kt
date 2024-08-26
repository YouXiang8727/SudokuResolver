package com.example.composeApp.mvi.sudoku

import com.example.composeApp.model.Cell
import com.example.composeApp.model.enums.RunState
import com.example.composeApp.mvi.Action

sealed interface SudokuAction: Action {
    data class SetRunState(val runState: RunState): SudokuAction

    data class SelectCell(val cell: Cell): SudokuAction

    data class InputValue(val value: Int, val isDefault: Boolean = true): SudokuAction

    data object ClearResult: SudokuAction

    data object ClearAll: SudokuAction

    data object EraseValue: SudokuAction

}