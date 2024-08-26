package com.example.composeApp.mvi.sudoku

import com.example.composeApp.model.Cell
import com.example.composeApp.mvi.Reducer

class SudokuReducer: Reducer<SudokuState, SudokuAction> {
    override fun reduce(state: SudokuState, action: SudokuAction): SudokuState {
        return when(action) {
            is SudokuAction.SetRunState -> setRunState(state, action)
            SudokuAction.ClearAll -> clearAll(state)
            SudokuAction.ClearResult -> clearResult(state)
            SudokuAction.EraseValue -> eraseValue(state)
            is SudokuAction.InputValue -> inputValue(state, action)
            is SudokuAction.SelectCell -> selectCell(state, action)
        }
    }

    private fun setRunState(
        state: SudokuState,
        action: SudokuAction.SetRunState
    ): SudokuState = state.copy(
        runState = action.runState
    )

    private fun clearAll(
        state: SudokuState
    ): SudokuState {
        return state.copy(
            cells = Array(9){ row ->
                Array(9){ col ->
                    Cell(
                        row,
                        col,
                        -1
                    )
                }
            }
        )
    }

    private fun clearResult(
        state: SudokuState
    ): SudokuState {
        return state.copy(
            cells = state.cells.map { cells ->
                cells.map { cell ->
                    if (cell.isDefault) cell.copy() else cell.copy(value = -1)
                }.toTypedArray()
            }.toTypedArray()
        )
    }

    private fun eraseValue(
        state: SudokuState
    ): SudokuState {
        state.selectedCell ?: return state.copy()
        return state.copy(
            cells = state.cells.map { cells ->
                cells.map { cell ->
                    if (cell == state.selectedCell) cell.copy(value = -1, isDefault = false) else cell.copy()
                }.toTypedArray()
            }.toTypedArray()
        )
    }

    private fun inputValue(
        state: SudokuState,
        action: SudokuAction.InputValue
    ): SudokuState {
        state.selectedCell ?: return state.copy()
        return state.copy(
            cells = state.cells.map { cells ->
                cells.map { cell ->
                    if (cell == state.selectedCell) cell.copy(value = action.value, isDefault = action.isDefault) else cell.copy()
                }.toTypedArray()
            }.toTypedArray()
        )
    }

    private fun selectCell(
        state: SudokuState,
        action: SudokuAction.SelectCell
    ): SudokuState {
        return state.copy(
            selectedCell = action.cell
        )
    }
}