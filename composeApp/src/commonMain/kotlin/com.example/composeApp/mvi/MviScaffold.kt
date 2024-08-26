package com.example.composeApp.mvi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

interface State

interface Action

interface Reducer<S : State, A : Action> {
    fun reduce(state: S, action: A): S
}

private const val bufferSize = 128

open class MviViewModel<S: State, A: Action>(
    private val reducer: Reducer<S, A>,
    initialState: S
): ViewModel() {

    private val actions = MutableSharedFlow<A>(
        extraBufferCapacity = bufferSize
    )

    var state: S by mutableStateOf(initialState)
        private set

    init {
        viewModelScope.launch {
            actions.collect { action ->
                state = reducer.reduce(state, action)
            }
        }
    }

    fun dispatch(action: A) {
        if (actions.tryEmit(action).not()) {
            error("MVI action buffer overflow")
        }
    }
}