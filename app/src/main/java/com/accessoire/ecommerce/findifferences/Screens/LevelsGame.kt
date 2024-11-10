package com.accessoire.ecommerce.findifferences.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.accessoire.ecommerce.findifferences.GameState

@Composable
fun LevelsGames(navController : NavHostController,level : MutableState<Int>) {
    val gameState = remember { GameState() }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center // Center content within the Box
        ) {
            Text(
                text = "Find All Differences",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
        }
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(gameState.levels.size) { index ->
                Card(
                    modifier = Modifier
                        .height(320.dp)
                        .padding(4.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(),
                    onClick = {}
                ) {
                    Box {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = gameState.levels[index].name,
                                style = MaterialTheme.typography.labelLarge

                            )
                            Image(
                                painter = painterResource(id = gameState.levels[index].image_1), // Placeholder or load image from URL
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .height(150.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        level.value = index
                                        navController.navigate("Main_Screen")
                                    }
                            )
                            Image(
                                painter = painterResource(id = gameState.levels[index].image_2), // Placeholder or load image from URL
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .height(150.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        level.value = index
                                        navController.navigate("Main_Screen")
                                    }
                            )
                        }

                        if (index > gameState.checkLevel(LocalContext.current))
                            lockedLevel()
                    }

                    // Other UI components can go here
                }
            }
        }
    }
}

@Preview
@Composable
fun lockedLevel() {
    Box(
        modifier = Modifier
            .height(320.dp)
            .fillMaxWidth()
            .clickable {

            }
            .background(
                Color.Black.copy(
                    alpha = .6f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = .7f
                ),
                contentColor = Color.Black,
                disabledContentColor = Color.LightGray,
                disabledContainerColor = Color.Black,
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock, contentDescription = "Lock",
                    modifier = Modifier
                        .padding(10.dp),
                    tint = Color.White
                )
                Text(
                    text = "Lock Level",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }

    }


}