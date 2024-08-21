package com.example.composeApp.model.enums

import kotlin.time.Duration

sealed class RunState(
    val isRunning: Boolean
) {
    data object Init: RunState(false)
    data object Running: RunState(true)
    data class Error(
        val message: String
    ): RunState(false)
    data class Finished(
        val times: Int,
        val measureTime: Duration
    ): RunState(false)
}