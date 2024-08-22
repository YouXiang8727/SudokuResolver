package com.example.composeApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeApp.model.Game
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val game = Game()

    val cells = game.cells
    val runState = game.runState
    val selectedCell = game.selectedCell

    fun inputValue(
        value: Int
    ) {
        game.inputValue(value)
    }

    fun setSelectedCell(row: Int, col: Int) {
        game.setSelectedCell(row, col)
    }

    fun clearAll() {
        game.clearAll()
    }

    fun clearResult() {
        game.clearResult()
    }

    fun run() {
        viewModelScope.launch {
            game.run()
        }
    }
}