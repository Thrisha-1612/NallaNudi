package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun SavedWordsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    var savedWords by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    val dao = remember {
        DatabaseInstance
            .getDatabase(context)
            .wordDao()
    }

    LaunchedEffect(Unit) {

        dao.getSavedWords()
            .collectLatest {

                savedWords = it
            }
    }

    val backgroundColor = Color(0xFFF6F8F5)
    val primaryGreen = Color(0xFF1B5E20)
    val softGreen = Color(0xFFE8F5E9)
    val darkText = Color(0xFF1C1C1C)
    val subtitleColor = Color(0xFF6B7280)
    val goldAccent = Color(0xFFFFC857)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(softGreen),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = primaryGreen
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {

                Text(
                    text = "My Learning List",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = darkText
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Saved technical terms for revision",
                    color = subtitleColor,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (savedWords.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📚",
                        fontSize = 42.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "No Saved Words Yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = darkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Bookmark difficult STEM words and revise them later using flashcards.",
                        color = subtitleColor,
                        fontSize = 14.sp
                    )
                }
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                items(savedWords) { word ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                navController.navigate(
                                    "word_detail/${word.word}"
                                )
                            },

                        shape = RoundedCornerShape(28.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.White,
                                            Color(0xFFF9FBF8)
                                        )
                                    )
                                )
                                .padding(22.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = word.word,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryGreen
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            goldAccent.copy(alpha = 0.15f)
                                        )
                                        .padding(
                                            horizontal = 10.dp,
                                            vertical = 6.dp
                                        )
                                ) {

                                    Text(
                                        text = word.category,
                                        color = Color(0xFF9A6700),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = word.kannadaMeaning,
                                fontSize = 20.sp,
                                color = Color(0xFF004D40),
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = word.meaning,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                color = subtitleColor
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}