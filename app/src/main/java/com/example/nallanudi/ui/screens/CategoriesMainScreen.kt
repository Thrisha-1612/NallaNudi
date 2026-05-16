package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class SubjectCategory(
    val name: String,
    val icon: String,
    val color: Color
)

@Composable
fun CategoriesMainScreen(
    navController: NavController
) {

    val categories = listOf(

        SubjectCategory(
            "Science",
            "🔬",
            Color(0xFF5C7CFA)
        ),

        SubjectCategory(
            "Math",
            "📐",
            Color(0xFF845EC2)
        ),

        SubjectCategory(
            "Physics",
            "⚛️",
            Color(0xFF4D8076)
        ),

        SubjectCategory(
            "Chemistry",
            "🧪",
            Color(0xFFC06C84)
        ),

        SubjectCategory(
            "Biology",
            "🧬",
            Color(0xFF4F8A8B)
        ),

        SubjectCategory(
            "Commerce",
            "📊",
            Color(0xFF6C757D)
        )
    )

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFEFF4FF)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Explore Subjects",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Browse technical terms by category",
            fontSize = 15.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(28.dp))

        LazyVerticalGrid(

            columns = GridCells.Fixed(2),

            verticalArrangement = Arrangement.spacedBy(18.dp),

            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            items(categories) { category ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable {

                            navController.navigate(
                                "category/${category.name.lowercase()}"
                            )
                        },

                    shape = RoundedCornerShape(30.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = category.color
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {

                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),

                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = category.icon,
                            fontSize = 42.sp
                        )

                        Column {

                            Text(
                                text = category.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Learn concepts",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}