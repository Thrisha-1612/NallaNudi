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
import com.example.nallanudi.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {

    Scaffold(

        bottomBar = {

            // ❌ Hide bottom bar on splash
            if (
                navController.currentBackStackEntry
                    ?.destination
                    ?.route != "splash"
            ) {

                BottomBar(navController)
            }
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,

            // ✅ SPLASH FIRST
            startDestination = "splash",

            modifier = Modifier.padding(paddingValues)
        ) {

            // ✅ SPLASH SCREEN
            composable("splash") {
                SplashScreen(navController)
            }

            // ✅ HOME
            composable("home") {
                HomeScreen(navController)
            }

            // ✅ CATEGORY MAIN
            composable("categories_main") {
                CategoriesMainScreen(navController)
            }

            // ✅ CATEGORY DETAILS
            composable(
                route = "category/{name}",

                arguments = listOf(
                    navArgument("name") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val name =
                    backStackEntry.arguments
                        ?.getString("name") ?: ""

                CategoryScreen(
                    navController = navController,
                    category = name
                )
            }

            // ✅ SAVED WORDS
            composable("saved_words") {
                SavedWordsScreen(navController)
            }

            // ✅ FLASHCARDS
            composable("flashcards") {
                FlashcardScreen()
            }

            // ✅ SEARCH
            composable(
                route = "search/{query}",

                arguments = listOf(
                    navArgument("query") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { backStackEntry ->

                val query =
                    backStackEntry.arguments
                        ?.getString("query") ?: ""

                SearchScreen(
                    navController = navController,
                    query = query
                )
            }

            // ✅ WORD DETAIL
            composable(
                route = "word_detail/{word}",

                arguments = listOf(
                    navArgument("word") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val word =
                    backStackEntry.arguments
                        ?.getString("word") ?: ""

                WordDetailScreen(
                    navController = navController,
                    word = word
                )
            }
        }
    }
}