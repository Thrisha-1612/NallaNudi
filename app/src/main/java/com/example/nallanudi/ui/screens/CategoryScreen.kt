package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CategoryScreen(
    navController: NavController,
    category: String
) {

    val context = LocalContext.current

    var words by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    LaunchedEffect(category) {

        dao.getWordsByCategory(category)
            .collectLatest {

                words = it
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp)
    ) {

        Text(
            text = category.replaceFirstChar { it.uppercase() },

            style = MaterialTheme.typography.headlineMedium,

            fontWeight = FontWeight.Bold,

            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (words.isEmpty()) {

            Text(
                text = "No words found",
                color = Color.Gray
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp)
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

                    shape = RoundedCornerShape(22.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = word.word,

                            style = MaterialTheme.typography.titleLarge,

                            fontWeight = FontWeight.Bold,

                            color = Color(0xFF223047)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = word.kannadaMeaning,

                            style = MaterialTheme.typography.titleMedium,

                            color = Color(0xFF4F6F52)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = word.meaning,

                            style = MaterialTheme.typography.bodyMedium,

                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}