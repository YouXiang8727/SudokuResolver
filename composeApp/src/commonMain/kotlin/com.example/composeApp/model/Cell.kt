package com.example.composeApp.model

data class Cell(
    val row: Int,
    val col: Int,
    val value: Int,
    val isValid: Boolean,
    val isDefault: Boolean = false
) {
    val showValue: String = if (value == -1) "" else value.toString()
}