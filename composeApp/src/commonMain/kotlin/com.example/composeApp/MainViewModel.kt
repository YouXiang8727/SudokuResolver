package com.example.composeApp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeApp.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val game = Game()

    val cells = game.cells
    val runState = game.runState

    private val _selectedCell: MutableStateFlow<Pair<Int, Int>?> = MutableStateFlow(null)
    val selectedCell: StateFlow<Pair<Int, Int>?> = _selectedCell.asStateFlow()

    fun inputValue(
        value: Int
    ) {
        selectedCell.value?.let {
            game.inputValue(it.first, it.second, value, value != -1)
        }
    }

    fun setSelectedCell(row: Int, col: Int) {
        _selectedCell.value = Pair(row, col)
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