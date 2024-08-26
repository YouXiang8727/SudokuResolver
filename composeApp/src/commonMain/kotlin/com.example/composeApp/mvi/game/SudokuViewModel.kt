package com.example.composeApp.mvi.game

import androidx.lifecycle.viewModelScope
import com.example.composeApp.model.Cell
import com.example.composeApp.model.common.Stack
import com.example.composeApp.model.enums.RunState
import com.example.composeApp.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.math.max
import kotlin.time.measureTime

class SudokuViewModel : MviViewModel<SudokuState, SudokuAction>(
    reducer = SudokuReducer(),
    initialState = SudokuState()
) {
    fun clearAll() {
        dispatch(SudokuAction.ClearAll)
    }

    fun clearResult() {
        dispatch(SudokuAction.ClearResult)
    }

    fun eraseValue() {
        dispatch(SudokuAction.EraseValue)
    }

    fun selectCell(cell: Cell) {
        dispatch(SudokuAction.SelectCell(cell))
    }

    fun inputValue(
        value: Int,
        isDefault: Boolean
    ) {
        dispatch(SudokuAction.InputValue(value, isDefault))
    }

    fun run() {
        viewModelScope.launch(Dispatchers.Default) {
            dispatch(SudokuAction.SetRunState(RunState.Running))

            var retryCount: Int
            val stack = Stack<Cell>()
            val measureTime = measureTime {
                withContext(Dispatchers.Main) {
                    retryCount = inputCell(getNextInputCell(), 0, stack)
                }
            }

            dispatch(
                SudokuAction.SetRunState(
                    if (stack.isEmpty()) {
                        RunState.Error("Could not resolve sudoku, please check your input and try again")
                    } else {
                        RunState.Finished(retryCount, measureTime)
                    }
                )
            )
        }
    }

    private suspend fun inputCell(
        cell: Cell?,
        tryCount: Int,
        stack: Stack<Cell>
    ): Int {
        cell ?: return tryCount

        var tryCountCopy = tryCount
        val inputValueAndFindValid = inputValueAndFindValid(
            cell,
            stack
        ) {
            tryCountCopy++
        }

        val nextInputCell = if (inputValueAndFindValid) getNextInputCell() else stack.pop()
        return inputCell(nextInputCell, tryCountCopy, stack)
    }

    private fun getNextInputCell(): Cell? {
        return state.cells.flatten().filter {
            it.value == -1
        }.minByOrNull { cell ->
            (1..9).filter { value ->
                cell.isValid(value, state.cells)
            }.size
        }
    }

    private suspend fun inputValueAndFindValid(
        cell: Cell,
        stack: Stack<Cell>,
        addRetryCount: () -> Unit
    ): Boolean {
        selectCell(cell)
        addRetryCount()
        val startValue = max(cell.value, 0) + 1

        val validCell = (startValue..9).firstOrNull {
            cell.isValid(it, state.cells)
        }

        if (validCell != null) {
            inputValue(validCell, false)
            yield()
            stack.push(Cell(cell.row, cell.col, validCell, false))
            return true
        }else {
            eraseValue()
            yield()
            return false
        }
    }
}