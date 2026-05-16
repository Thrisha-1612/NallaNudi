package com.example.nallanudi.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
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
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlashcardScreen() {

    val context = LocalContext.current

    val dao = remember {
        DatabaseInstance
            .getDatabase(context)
            .wordDao()
    }

    var savedWords by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    LaunchedEffect(Unit) {

        dao.getSavedWords().collectLatest {
            savedWords = it
        }
    }

    if (savedWords.isEmpty()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F7FB)),

            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "📚",
                    fontSize = 54.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "No Saved Flashcards",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1F24)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Save words to start revising",
                    color = Color.Gray
                )
            }
        }

        return
    }

    val pagerState = rememberPagerState {
        savedWords.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
            .padding(vertical = 24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Flashcard Revision",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B1F24)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Swipe through your saved technical terms",
            color = Color(0xFF6B7280),
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(34.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->

            val word = savedWords[page]

            var showMeaning by remember(page) {
                mutableStateOf(false)
            }

            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(460.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .clickable {
                        showMeaning = !showMeaning
                    },

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),

                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(0xFF2C3E50),
                                    Color(0xFF4CA1AF)
                                )
                            )
                        )
                        .padding(30.dp),

                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = word.word,
                            fontSize = 34.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Text(
                            text = word.kannadaMeaning,
                            fontSize = 26.sp,
                            color = Color.White.copy(alpha = 0.92f),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        if (showMeaning) {

                            Text(
                                text = word.meaning,
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.96f),
                                lineHeight = 28.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = word.category,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )

                        } else {

                            Text(
                                text = "Tap card to reveal meaning",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "${pagerState.currentPage + 1} / ${savedWords.size}",
            color = Color(0xFF6B7280),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}