package com.example.nallanudi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(

        BottomNavItem(
            "home",
            Icons.Default.Home,
            "Home"
        ),

        BottomNavItem(
            "categories_main",
            Icons.Default.Category,
            "Categories"
        ),

        BottomNavItem(
            "flashcards",
            Icons.Default.Style,
            "Flashcards"
        ),

        BottomNavItem(
            "saved_words",
            Icons.Default.Bookmark,
            "Saved"
        )
    )

    val navBackStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            )
    ) {

        NavigationBar(

            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(28.dp),
                    clip = false
                ),

            containerColor = Color.White,

            tonalElevation = 0.dp
        ) {

            items.forEach { item ->

                val selected = currentRoute == item.route

                NavigationBarItem(

                    selected = selected,

                    onClick = {

                        navController.navigate(item.route) {

                            popUpTo(navController.graph.startDestinationId)

                            launchSingleTop = true
                        }
                    },

                    icon = {

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },

                    label = {

                        Text(item.label)
                    },

                    alwaysShowLabel = true,

                    colors = NavigationBarItemDefaults.colors(

                        selectedIconColor = Color.White,

                        selectedTextColor = Color(0xFF355C7D),

                        unselectedIconColor = Color.Gray,

                        unselectedTextColor = Color.Gray,

                        indicatorColor = Color(0xFF355C7D)
                    )
                )
            }
        }
    }
}