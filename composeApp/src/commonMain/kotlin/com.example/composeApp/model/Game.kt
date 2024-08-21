package com.example.composeApp.model

import com.example.composeApp.model.enums.RunState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.yield
import kotlin.math.max
import kotlin.time.measureTime

class Game {
    private val _cells: MutableStateFlow<Array<Array<Cell>>> = MutableStateFlow(
        Array(9) { x ->
            Array(9) { y ->
                Cell(
                    x,
                    y,
                    -1,
                    true
                )
            }
        }
    )

    val cells: StateFlow<Array<Array<Cell>>> = _cells.asStateFlow()

    private val _runState: MutableStateFlow<RunState> = MutableStateFlow(RunState.Init)
    val runState: StateFlow<RunState> = _runState.asStateFlow()

    fun inputValue(
        row: Int,
        col: Int,
        value: Int,
        isDefault: Boolean = false
    ) {
        _cells.value = cells.value.copyOf().apply {
            this[row][col] = Cell(
                row,
                col,
                value,
                isValid(row, col, value),
                isDefault
            )
        }
    }

    private fun isValid(
        row: Int,
        col: Int,
        value: Int
    ): Boolean {
        if (value == -1) return true
        return cells.value.flatten()
            .filterNot {
                it.row == row && it.col == col
            }.any {
                it.value == value &&
                        (it.row == row || it.col == col ||
                                (it.row / 3 == row / 3 && it.col / 3 == col / 3))
            }.not()
    }

    suspend fun run() {
        _runState.value = RunState.Running
        if (cells.value.flatten().any {
                it.isValid.not()
            }) {
            _runState.value = RunState.Error("Sudoku is not valid with invalid cell(s)")
            return
        }

        val needInputCells = cells.value.flatten().filterNot {
            it.isDefault
        }.map {
            Pair(it.row, it.col)
        }

        var currentIndex = 0

        var retryCount = 0
        val measureTime = measureTime {
            while (currentIndex >= 0 && currentIndex < needInputCells.size) {
                val row = needInputCells[currentIndex].first
                val col = needInputCells[currentIndex].second

                val startValue = max(cells.value[row][col].value, 0) + 1

                val findValid: Boolean = run findValid@{
                    for (i in startValue..9) {
                        retryCount++
                        val isValid = isValid(row, col, i)
                        inputValue(row, col, i)
                        if (isValid) return@findValid true
                    }
                    false
                }

                if (findValid) {
                    currentIndex++
                } else {
                    inputValue(row, col, -1)
                    currentIndex--
                }
                yield()
            }
        }

        _runState.value = if (currentIndex < 0) {
            RunState.Error("Could not resolve sudoku, please check your input and try again")
        } else {
            RunState.Finished(retryCount, measureTime)
        }
    }

    fun clearResult() {
        _cells.value = cells.value.map {
            it.map {
                it.copy(
                    value = if (it.isDefault) it.value else -1,
                    isValid = if (it.isDefault) it.isValid else true
                )
            }.toTypedArray()
        }.toTypedArray()
    }

    fun clearAll() {
        _cells.value = Array(9) { row ->
            Array(9) { col ->
                Cell(
                    row,
                    col,
                    -1,
                    true
                )
            }
        }
        _runState.value = RunState.Init
    }
}
