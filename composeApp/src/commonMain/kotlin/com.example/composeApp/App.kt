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
import com.example.composeApp.ui.Sudoku
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    viewModel: MainViewModel = viewModel { MainViewModel() }
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
                val gameState = viewModel.runState.collectAsStateWithLifecycle()
                val cells = viewModel.cells.collectAsStateWithLifecycle()

                LaunchedEffect(gameState.value) {
                    when(val state = gameState.value) {
                        is RunState.Error -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                state.message
                            )
                        }
                        is RunState.Finished -> {
                            scaffoldState.snackbarHostState.showSnackbar(
                                "Finished with ${state.times} retries in ${state.measureTime}ms"
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
                            viewModel.inputValue(-1)
                        },
                        enabled = gameState.value.isRunning.not()
                    ) {
                        Text(
                            text = "Erased"
                        )
                    }

                    TextButton(
                        onClick = {
                            viewModel.clearResult()
                        },
                        enabled = gameState.value.isRunning.not()
                    ) {
                        Text("Clear Result")
                    }

                    TextButton(
                        onClick = {
                            viewModel.clearAll()
                        },
                        enabled = gameState.value.isRunning.not()
                    ) {
                        Text(
                            "Clear All"
                        )
                    }

                    TextButton(
                        onClick = {
                            if (cells.value.flatten().any() {
                                it.isDefault.not() && it.value != -1
                            }) {
                                scope.launch {
                                    scaffoldState.snackbarHostState
                                        .showSnackbar(
                                            "Please clear result first"
                                        )
                                }
                            }else {
                                viewModel.run()
                            }
                        },
                        enabled = gameState.value.isRunning.not()
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
                    val selectedCell = viewModel.selectedCell.collectAsStateWithLifecycle()
                    for (i in 1..9) {
                        TextButton(
                            modifier = Modifier.weight(1f)
                                .aspectRatio(1f),
                            onClick = {
                                viewModel.inputValue(i)
                            },
                            enabled = selectedCell.value != null
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