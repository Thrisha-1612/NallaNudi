package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navController: NavController,
    query: String
) {

    val context = LocalContext.current

    var words by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFEFF4FF)
        )
    )

    LaunchedEffect(query) {

        val db = DatabaseInstance.getDatabase(context)

        db.wordDao()
            .searchWords(query)
            .collectLatest {

                words = it
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Search Results",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Results for \"$query\"",
            color = Color.Gray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (words.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),

                horizontalAlignment = Alignment.CenterHorizontally,

                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "No words found",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Try another technical term",
                    color = Color.LightGray
                )
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(words) { word ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                navController.navigate(
                                    "word_detail/${word.word}"
                                )
                            },

                        shape = RoundedCornerShape(24.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(22.dp)
                        ) {

                            Text(
                                text = word.word,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF355C7D)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = word.kannadaMeaning,
                                fontSize = 18.sp,
                                color = Color(0xFF4F6F52),
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = word.meaning,
                                color = Color(0xFF475569),
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Surface(
                                color = Color(0xFFE8EEF9),
                                shape = RoundedCornerShape(50)
                            ) {

                                Text(
                                    text = word.category,
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 6.dp
                                    ),
                                    fontSize = 12.sp,
                                    color = Color(0xFF355C7D)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}