package com.accessoire.ecommerce.findifferences

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

class GameState {

    var level1Differences = mutableStateListOf<Difference>()

    init {
        // Initialize differences list
        level1Differences.addAll(
            listOf(
                Difference(x = 0.157f, y = 0.789f, radius = 0.05f),
                Difference(x = 0.936f, y = 0.682f, radius = 0.05f),
                Difference(x = 0.257f, y = 0.553f, radius = 0.05f),
                Difference(x = 0.372f, y = 0.553f, radius = 0.05f),
                Difference(x = 0.559f, y = 0.082f, radius = 0.05f)
            )
        )
    }
    @Composable
    fun DifferenceCircles(differences: List<Difference>, imageSize: IntSize, isHint:Boolean) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            differences.forEach { difference ->
                if (difference.isFoundState) {
                    drawCircle(
                        color = Color.Red,
                        center = Offset(
                            x = difference.x * imageSize.width,
                            y = difference.y * imageSize.height
                        ),
                        radius = difference.radius * imageSize.width,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }
        }
    }


    var lives by mutableStateOf(3)
    var foundDifferences by mutableStateOf(0)
    var totalDifferences = level1Differences.size

    fun foundDifference() {
        foundDifferences++
    }

    fun loseLife() {
        lives--
    }

    fun resetGame() {
        lives = 3
        foundDifferences = 0
        level1Differences.forEach { it.isFoundState = false }
    }

    fun isGameOver(): Boolean = lives <= 0 || foundDifferences == totalDifferences
}