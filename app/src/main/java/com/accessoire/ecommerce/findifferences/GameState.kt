package com.accessoire.ecommerce.findifferences

import android.content.Context
import android.content.SharedPreferences
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
    lateinit var mySharedPref: SharedPreferences
    var levels = mutableStateListOf<Level>()

    init {
        levels.addAll(
            listOf(
                // level 1
                Level(
                    "level_1",
                    R.drawable.image_1,
                    R.drawable.image_2,
                    listOf(
                        Difference(x = 0.157f, y = 0.789f, radius = 0.05f),
                        Difference(x = 0.936f, y = 0.682f, radius = 0.05f),
                        Difference(x = 0.257f, y = 0.553f, radius = 0.05f),
                        Difference(x = 0.372f, y = 0.553f, radius = 0.05f),
                        Difference(x = 0.559f, y = 0.082f, radius = 0.05f)
                    )
                ),
                // level 2
                Level(
                    "level_2",
                    R.drawable.farm_1,
                    R.drawable.farm_2,
                    listOf(
                        Difference(x = 0.803f, y = 0.067f, radius = 0.05f),
                        Difference(x = 0.49f, y =0.3487f, radius = 0.05f),
                        Difference(x = 0.1584f, y = 0.534f, radius = 0.05f),
                        Difference(x = 0.359f, y = 0.638f, radius = 0.05f),
                        Difference(x = 0.113f, y = 0.266f, radius = 0.05f),
                        Difference(x = 0.803f, y = 0.694f, radius = 0.05f),
                        Difference(x = 0.895f, y = 0.5169f, radius = 0.05f),
                        Difference(x = 0.752f, y = 0.3047f, radius = 0.05f),
                        Difference(x = 0.63f, y = 0.5124f, radius = 0.05f)
                    )
                ),
                // level 3
                Level(
                    "level_3",
                    R.drawable.nature_1,
                    R.drawable.nature_2,
                    listOf(
                        Difference(x = 0.1045f, y =0.0595f, radius = 0.05f),
                        Difference(x = 0.517f, y = 0.55f, radius = 0.05f),
                        Difference(x = 0.484f, y = 0.084f, radius = 0.05f),
                        Difference(x = 0.85f, y = 0.539f, radius = 0.05f),
                        Difference(x = 0.812f, y = 0.786f, radius = 0.05f)
                    )
                )
            )
        )
    }

    fun checkLevel(context: Context): Int {
        mySharedPref = context.getSharedPreferences("Level", Context.MODE_PRIVATE)
        val level_ = mySharedPref.getInt("Level", 0)
        return level_
    }

    fun addLevel(context: Context, level: Int) {
        mySharedPref = context.getSharedPreferences("Level", Context.MODE_PRIVATE)
        val editor = mySharedPref.edit()
        editor.putInt("Level", level)
        editor.apply()
    }

    @Composable
    fun DifferenceCircles(differences: List<Difference>, imageSize: IntSize, isHint: Boolean) {
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
    fun totalDifferences(index: Int) = levels[index].difference.size

//    var totalDifferences = levels[index].difference.size

    fun foundDifference() {
        foundDifferences++
    }

    fun loseLife() {
        lives--
    }

    fun resetGame(index: Int) {
        lives = 3
        foundDifferences = 0
        levels[index].difference.forEach { it.isFoundState = false }
    }

    fun isGameOver(index: Int): Boolean = lives <= 0 || foundDifferences == totalDifferences(index)
}