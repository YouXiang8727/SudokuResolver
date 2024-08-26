package com.example.composeApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeApp.model.enums.RunState
import com.example.composeApp.mvi.game.SudokuViewModel
import com.example.composeApp.ui.Sudoku
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    viewModel: SudokuViewModel = viewModel { SudokuViewModel() }
) {
    MaterialTheme {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LaunchedEffect(viewModel.state.runState) {
                    when(val state = viewModel.state.runState) {
                        is RunState.Error -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                state.message
                            )
                        }
                        is RunState.Finished -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                "Finished with ${state.times} retries in ${state.measureTime}"
                            )
                        }
                        else -> {}
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {
                            viewModel.eraseValue()
                        },
                        enabled = viewModel.state.runState.isRunning.not()
                    ) {
                        Text(
                            text = "Erased"
                        )
                    }

                    TextButton(
                        onClick = {
                            viewModel.clearResult()
                        },
                        enabled = viewModel.state.runState.isRunning.not()
                    ) {
                        Text("Clear Result")
                    }

                    TextButton(
                        onClick = {
                            viewModel.clearAll()
                        },
                        enabled = viewModel.state.runState.isRunning.not()
                    ) {
                        Text(
                            "Clear All"
                        )
                    }

                    TextButton(
                        onClick = {
                            if (viewModel.state.isResultCleared.not()) {
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Please clear result first"
                                    )
                                }
                                return@TextButton
                            }

                            if (viewModel.state.isValidSudoku.not()) {
                                scope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Sudoku is not valid with invalid cell(s)"
                                    )
                                }
                                return@TextButton
                            }
                            viewModel.run()
                        },
                        enabled = viewModel.state.runState.isRunning.not()
                    ) {
                        Text(
                            "Run"
                        )
                    }
                }

                Sudoku()

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (i in 1..9) {
                        TextButton(
                            modifier = Modifier.weight(1f)
                                .aspectRatio(1f),
                            onClick = {
                                viewModel.inputValue(i, true)
                            },
                            enabled = viewModel.state.selectedCell != null &&
                                    viewModel.state.runState.isRunning.not()
                        ) {
                            Text(
                                text = i.toString(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}