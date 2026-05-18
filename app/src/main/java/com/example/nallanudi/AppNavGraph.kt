package com.example.nallanudi

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.nallanudi.ui.components.BottomBar
import com.example.nallanudi.ui.screens.CategoriesMainScreen
import com.example.nallanudi.ui.screens.CategoryScreen
import com.example.nallanudi.ui.screens.FlashcardScreen
import com.example.nallanudi.ui.screens.HomeScreen
import com.example.nallanudi.ui.screens.SavedWordsScreen
import com.example.nallanudi.ui.screens.SearchScreen
import com.example.nallanudi.ui.screens.WordDetailScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            // HOME
            composable("home") {
                HomeScreen(navController)
            }

            // CATEGORIES MAIN
            composable("categories_main") {
                CategoriesMainScreen(navController)
            }

            // CATEGORY SCREEN
            composable(
                route = "category/{name}",
                arguments = listOf(
                    navArgument("name") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val category =
                    backStackEntry.arguments?.getString("name") ?: ""

                CategoryScreen(
                    navController = navController,
                    category = category
                )
            }

            // SAVED WORDS
            composable("saved_words") {
                SavedWordsScreen(navController)
            }

            // FLASHCARDS
            composable("flashcards") {
                FlashcardScreen()
            }

            // SEARCH
            composable(
                route = "search/{query}",
                arguments = listOf(
                    navArgument("query") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val query =
                    backStackEntry.arguments?.getString("query") ?: ""

                SearchScreen(
                    navController = navController,
                    query = query
                )
            }

            // WORD DETAIL
            composable(
                route = "word_detail/{word}",
                arguments = listOf(
                    navArgument("word") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val word =
                    backStackEntry.arguments?.getString("word") ?: ""

                WordDetailScreen(
                    navController = navController,
                    word = word
                )
            }
        }
    }
}