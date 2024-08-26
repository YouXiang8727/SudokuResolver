package com.example.composeApp.model

import com.example.composeApp.model.common.Stack
import com.example.composeApp.model.enums.RunState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.yield
import kotlin.math.max
import kotlin.time.measureTime

class Game {
//    private val _cells: MutableStateFlow<Array<Array<Cell>>> = MutableStateFlow(
//        Array(9) { x ->
//            Array(9) { y ->
//                Cell(
//                    x,
//                    y,
//                    -1,
//                    true
//                )
//            }
//        }
//    )
//
//    val cells: StateFlow<Array<Array<Cell>>> = _cells.asStateFlow()
//
//    private val _selectedCell: MutableStateFlow<Cell?> = MutableStateFlow(null)
//    val selectedCell: StateFlow<Cell?> = _selectedCell.asStateFlow()
//
//    private val _runState: MutableStateFlow<RunState> = MutableStateFlow(RunState.Init)
//    val runState: StateFlow<RunState> = _runState.asStateFlow()
//
//    private val inputCellStack: Stack<Cell> = Stack()
//
//    private fun inputValue(
//        cell: Cell,
//        value: Int,
//        isDefault: Boolean = false
//    ) {
//        _cells.value = cells.value.copyOf().apply {
//            val isValid = isValid(cell, value)
//            val c = cell.copy(value = value, isValid = isValid, isDefault = isDefault)
//            this[cell.row][cell.col] = c
//
//            if (isValid && value != -1 && isDefault.not()) {
//                inputCellStack.push(c)
//            }
//        }
//    }
//
//    private fun isValid(
//        cell: Cell,
//        value: Int
//    ): Boolean {
//        if (value == -1) return true
//        return cells.value.flatten()
//            .filterNot {
//                it.row == cell.row && it.col == cell.col
//            }.any {
//                it.value == value &&
//                        (it.row == cell.row || it.col == cell.col ||
//                                (it.row / 3 == cell.row / 3 && it.col / 3 == cell.col / 3))
//            }.not()
//    }
//
//    suspend fun run() {
//        _runState.value = RunState.Running
//        if (cells.value.flatten().any {
//                it.isValid.not()
//            }) {
//            _runState.value = RunState.Error("Sudoku is not valid with invalid cell(s)")
//            return
//        }
//
//        var retryCount: Int
//        val measureTime = measureTime {
//            retryCount = inputCell(getNextInputCell())
//        }
//
//        _runState.value = if (inputCellStack.isEmpty()) {
//            RunState.Error("Could not resolve sudoku, please check your input and try again")
//        } else {
//            RunState.Finished(retryCount, measureTime)
//        }
//    }
//
//    private suspend fun inputValueAndFindValid(cell: Cell, addRetryCount: () -> Unit): Boolean {
//        setSelectedCell(cell)
//        val startValue = max(cell.value, 0) + 1
//
//        for (i in startValue..9) {
//            val isValid = isValid(cell, i)
//            inputValue(cell, i)
//            yield()
//            addRetryCount()
//            if (isValid) return true
//        }
//        inputValue(cell, -1)
//        yield()
//        return false
//    }
//
//    private fun getNextInputCell(): Cell? {
//        return cells.value.flatten().filter {
//            it.value == -1
//        }.minByOrNull { cell ->
//            (1..9).filter { value ->
//                isValid(cell, value)
//            }.size
//        }
//    }
//
//    private suspend fun inputCell(cell: Cell?, tryCount: Int = 0): Int {
//        cell ?: return tryCount
//
//        var tryCountCopy = tryCount
//        val inputValueAndFindValid = inputValueAndFindValid(
//            cell
//        ) {
//            tryCountCopy++
//        }
//
//        val nextInputCell = if (inputValueAndFindValid) getNextInputCell() else inputCellStack.pop()
//        return inputCell(nextInputCell, tryCountCopy)
//    }
//
//    fun clearResult() {
//        _cells.value = cells.value.map {
//            it.map {
//                it.copy(
//                    value = if (it.isDefault) it.value else -1,
//                    isValid = if (it.isDefault) it.isValid else true
//                )
//            }.toTypedArray()
//        }.toTypedArray()
//    }
//
//    fun clearAll() {
//        _cells.value = Array(9) { row ->
//            Array(9) { col ->
//                Cell(
//                    row,
//                    col,
//                    -1,
//                    true
//                )
//            }
//        }
//        _runState.value = RunState.Init
//    }
//
//    fun inputValue(
//        value: Int
//    ) {
//        selectedCell.value?.let {
//            inputValue(it, value, value != -1)
//        }
//    }
//
//    fun setSelectedCell(cell: Cell) {
//        _selectedCell.value = cell
//    }
}
