package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.flow.collectLatest

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

@Composable
fun FlashcardScreen() {

    val context = LocalContext.current

    var savedWords by remember { mutableStateOf<List<WordEntity>>(emptyList()) }

    val db = remember {
        DatabaseInstance.getDatabase(context)
    }

    // Load saved words
    LaunchedEffect(Unit) {
        db.wordDao()
            .getSavedWords()
            .collectLatest {
                savedWords = it
            }
    }

    if (savedWords.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No saved words for flashcards")
        }

        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { savedWords.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->

        val word = savedWords[page]

        FlashCardItem(word)
    }
}

@Composable
fun FlashCardItem(word: WordEntity) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B5E20)
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = word.word,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

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