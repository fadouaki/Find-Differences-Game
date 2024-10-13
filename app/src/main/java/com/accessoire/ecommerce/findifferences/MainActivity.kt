package com.accessoire.ecommerce.findifferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.accessoire.ecommerce.findifferences.Screens.FindDifferencesGame
import com.accessoire.ecommerce.findifferences.Screens.LevelsGames
import com.accessoire.ecommerce.findifferences.Screens.SplashScreen
import com.accessoire.ecommerce.findifferences.ui.theme.FinDifferencesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinDifferencesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash_screen") {
                    composable("splash_screen") {
                        SplashScreen(navController)
                    }
                    composable("Main_Screen") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Column(modifier = Modifier.padding(innerPadding))
                            {
                                HomeScreen()
                            }
                        }
                    }
                    composable("Level_Screen") {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Column(modifier = Modifier.padding(innerPadding))
                            {
                                LevelsGames(navController)
                            }
                        }

                    }
                }

            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FindDifferencesGame()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinDifferencesTheme {
        HomeScreen()
    }
}