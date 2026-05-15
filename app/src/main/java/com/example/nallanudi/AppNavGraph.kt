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
            BottomBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("home") {
                HomeScreen(navController)
            }

            composable("categories_main") {
                CategoriesMainScreen(navController)
            }




            composable(
                route = "category/{name}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                val name = backStackEntry.arguments?.getString("name")

                CategoryScreen(
                    name = name,
                    navController = navController
                )
            }

            composable(
                route = "search/{query}",
                arguments = listOf(
                    navArgument("query") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { backStackEntry ->

                val query = backStackEntry.arguments?.getString("query")

                SearchScreen(
                    navController = navController,
                    query = query
                )
            }

            composable(
                route = "word_detail/{word}",
                arguments = listOf(
                    navArgument("word") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                val word = backStackEntry.arguments?.getString("word")

                WordDetailScreen(
                    navController = navController,
                    word = word
                )
            }
        }
    }
}