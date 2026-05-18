package com.example.nallanudi.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(

        BottomNavItem(
            route = "home",
            title = "Home",
            icon = Icons.Default.Home
        ),

        BottomNavItem(
            route = "categories_main",
            title = "Categories",
            icon = Icons.Default.Category
        ),

        BottomNavItem(
            route = "flashcards",
            title = "Flashcards",
            icon = Icons.Default.FlashOn
        ),

        BottomNavItem(
            route = "saved_words",
            title = "Saved",
            icon = Icons.Default.Bookmark
        )
    )

    NavigationBar {

        val navBackStackEntry =
            navController.currentBackStackEntryAsState()

        val currentRoute =
            navBackStackEntry.value?.destination?.route

        items.forEach { item ->

            NavigationBarItem(

                selected = currentRoute == item.route,

                onClick = {

                    navController.navigate(item.route) {

                        popUpTo(navController.graph.startDestinationId)

                        launchSingleTop = true
                    }
                },

                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },

                label = {
                    Text(item.title)
                }
            )
        }
    }
}