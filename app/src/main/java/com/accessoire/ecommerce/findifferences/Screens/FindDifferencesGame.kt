package com.accessoire.ecommerce.findifferences.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.accessoire.ecommerce.findifferences.Difference
import com.accessoire.ecommerce.findifferences.GameState
import com.accessoire.ecommerce.findifferences.R
import kotlinx.coroutines.delay
import kotlin.math.sqrt

@Composable
fun FindDifferencesGame(
    navHostController: NavHostController,
    level: MutableState<Int>
) {
    val context = LocalContext.current
    val gameState = remember { GameState() }
    var imageSize = remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HeartsAndProgress(gameState, level)

        ImagesComparaison(
            context = context,
            gameState = gameState,
            level = level,
            imageSize = imageSize,
            modifier = Modifier.weight(1f)
        )

        BottomButton(gameState, level)
    }

    if (gameState.isGameOver(level.value)) {
        GameOverDialog(
            context = context,
            gameState = gameState,
            level = level,
            won = gameState.foundDifferences == gameState.totalDifferences(level.value),
            onPlayAgain = {
                gameState.resetGame(level.value)

            },
            nextLevel = {
                gameState.resetGame(level.value)
                navHostController.popBackStack()
            }
        )
    }
}


@Composable
fun ImagesComparaison(
    context: Context,
    level: MutableState<Int>,
    gameState: GameState,
    imageSize: MutableState<IntSize>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(10.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = Color.Black,
            disabledContentColor = Color.LightGray,
            disabledContainerColor = Color.Black,
        ),
    ) {
        // Images
        Column(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 5.dp)
            ) {
                Image(
                    painter = painterResource(id =gameState.levels.get(level.value).image_1),
                    contentDescription = "Room Image 1",
                    contentScale = androidx.compose.ui.layout.ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                )
                gameState.DifferenceCircles(
                    gameState.levels[level.value].difference,
                    imageSize.value,
                    false
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = gameState.levels.get(level.value).image_2),
                    contentDescription = "Room Image 2",
                    contentScale = androidx.compose.ui.layout.ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .onSizeChanged { imageSize.value = it }
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                handleImageTap(context, gameState, level, offset, imageSize.value)
                            }
                        }
                )
                gameState.DifferenceCircles(
                    gameState.levels[level.value].difference,
                    imageSize.value,
                    false
                )
            }
        }
    }
}


@Composable
fun HeartsAndProgress(
    gameState: GameState,
    level: MutableState<Int>
) {
    Card(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContentColor = Color.LightGray,
            disabledContainerColor = Color.Black,
        ),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
            .height(45.dp),
        shape = RoundedCornerShape(42.dp),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                repeat(gameState.lives) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Life",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Box(
                modifier = Modifier.weight(1f)
            ) {

                GameTimer()
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(gameState.totalDifferences(level.value) - gameState.foundDifferences) { index ->
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Progress",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

    }

}

@Composable
fun BottomButton(
    gameState: GameState,
    level: MutableState<Int>
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    getHint(gameState, level)
                    gameState.loseLife()
                    gameState.foundDifference()
                },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = Color.Blue,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Green
                )
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.hint_ic),
                        contentDescription = "hint"
                    )
                    Text(text = "  Hint")
                }

            }
            Button(
                onClick = { gameState.resetGame(level.value) },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = Color.Blue,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.Green
                )
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.replay_ic),
                        contentDescription = "hint"
                    )
                    Text(text = "  Replay")
                }

            }

        }
        Row(
            modifier = Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(5.dp),
                tint = Color.Red,
                painter = painterResource(id = R.drawable.warning_ic),
                contentDescription = "warning"
            )
            Text(
                modifier = Modifier.padding(end = 5.dp),
                style = MaterialTheme.typography.labelMedium,
                text = "if you clicked in hint we will increment the heart"
            )
        }

    }

}

fun getHint(
    gameState: GameState,
    level: MutableState<Int>
) {
    gameState.levels[level.value].difference.forEach { difference ->
        if (!difference.isFoundState) {
            difference.isFoundState = true
            return
        }
    }
}

@Composable
fun GameOverDialog(
    context: Context,
    gameState: GameState,
    level: MutableState<Int>,
    won: Boolean,
    onPlayAgain: () -> Unit,
    nextLevel: () -> Unit
) {
    if (won) {
        Log.d("Takkki", "woon")
        playSound(context, R.raw.win)
        if (gameState.checkLevel(context) < level.value + 1)
            gameState.addLevel(context, level.value + 1)
    } else
        playSound(context, R.raw.lose_game)

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = if (won) "Congratulations!" else "Game Over") },
        text = { Text(text = if (won) "You found all the differences!" else "Better luck next time!") },
        confirmButton = {
            Button(
                onClick =
                if (won)
                    nextLevel
                else
                    onPlayAgain
            ) {
                Text(
                    if (won)
                        "Next Level"
                    else
                        "Play Again"
                )
            }
        }
    )


}

@SuppressLint("DefaultLocale")
@Composable
fun GameTimer() {
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds++
        }
    }

    // Calculate minutes and seconds
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    // Format the time as MM:SS
    val timeFormatted = String.format("%02d:%02d", minutes, remainingSeconds)

    Text(text = "Time: $timeFormatted")
}


fun isWithinDifference(touch: Offset, difference: Difference, imageSize: IntSize): Boolean {
    Log.d("DifferenceTaki", "isWithinDifference: ${ imageSize.height } ")
    val dx = touch.x - difference.x * imageSize.width
    val dy = touch.y - difference.y * imageSize.height
    val isWithin = sqrt(dx * dx + dy * dy) <= difference.radius * imageSize.width
    Log.d("DifferenceTaki", "isWithinDifference: $isWithin ")
    return isWithin
}

fun handleImageTap(
    context: Context,
    gameState: GameState,
    level: MutableState<Int>,
    offset: Offset,
    imageSize: IntSize
) {
    for (difference in gameState.levels[level.value].difference) {
        if (!difference.isFound && isWithinDifference(offset, difference, imageSize)) {
            difference.isFoundState = true
            gameState.foundDifference()
            playSound(context, R.raw.find) // Play sound for losing a life
            return
        }
    }
    gameState.loseLife()
    playSound(context, R.raw.lose) // Play sound for losing a life
    vibrate(context)

}

fun playSound(context: Context, soundResId: Int) {
    val mediaPlayer = MediaPlayer.create(context, soundResId)
    mediaPlayer.setOnCompletionListener {
        it.release() // Release the MediaPlayer once the sound is done playing
    }
    mediaPlayer.start()
}

fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(100)
    }
}



