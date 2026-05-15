package com.example.nallanudi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.ui.model.CategoryItem

@Composable
fun CategoriesMainScreen(navController: NavController) {

    val categories = listOf(
        CategoryItem("Science", Color(0xFFE8F5E9)),
        CategoryItem("Math", Color(0xFFFFF3E0)),
        CategoryItem("Physics", Color(0xFFE3F2FD)),
        CategoryItem("Chemistry", Color(0xFFF3E5F5)),
        CategoryItem("Biology", Color(0xFFFFEBEE)),
        CategoryItem("Commerce", Color(0xFFE0F7FA))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(categories) { category ->

                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .clickable {
                            navController.navigate("category/${category.name}")
                        },
                    colors = CardDefaults.cardColors(containerColor = category.color)
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {

                        Text(category.name)
                    }
                }
            }
        }
    }
}