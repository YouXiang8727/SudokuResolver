package com.example.composeApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeApp.MainViewModel

@Composable
fun Sudoku(
    viewModel: MainViewModel = viewModel { MainViewModel() }
) {
    val cells = viewModel.cells.collectAsStateWithLifecycle()
    val selectedCell = viewModel.selectedCell.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
            .border(4.dp, Color.Black)
    ) {
        cells.value.forEachIndexed { row, cells ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
            ) {
                cells.forEachIndexed { col, cell ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                viewModel.setSelectedCell(
                                    row,
                                    col
                                )
                            }.then(
                                if (
                                    row == selectedCell.value?.first &&
                                    col == selectedCell.value?.second
                                ) {
                                    Modifier.background(Color.LightGray)
                                }else {
                                    Modifier.background(Color.White)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cell.showValue,
                            color = if (cell.isValid) {
                                Color.Black
                            }else {
                                Color.Red
                            },
                            fontWeight = if (cell.isDefault) FontWeight.Black else null
                        )
                    }

                    Divider(
                        modifier = Modifier.fillMaxHeight()
                            .then(
                                if(col % 3 == 2) {
                                    Modifier.width(2.dp)
                                        .background(Color.Black)
                                }else {
                                    Modifier.width(1.dp)
                                        .background(Color.LightGray)
                                }
                            )
                    )
                }
            }

            Divider(
                modifier = Modifier.fillMaxWidth()
                    .then(
                        if(row % 3 == 2) {
                            Modifier.height(2.dp)
                                .background(Color.Black)
                        }else {
                            Modifier.height(1.dp)
                                .background(Color.LightGray)
                        }
                    )
            )
        }
    }
}