package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FlashcardScreen() {

    val context = LocalContext.current

    var words by remember { mutableStateOf<List<WordEntity>>(emptyList()) }

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    // ✅ LIVE AUTO UPDATE (saved words only)
    LaunchedEffect(Unit) {
        dao.getSavedWords().collectLatest {
            words = it
        }
    }

    // ❌ EMPTY STATE
    if (words.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "No saved words yet!\nSave words to start learning 📚",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }

        return
    }

    val pagerState = rememberPagerState(
        pageCount = { words.size }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        Text(
            text = "Flashcards",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // SWIPE CARDS
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->

            FlashCardItem(word = words[page])
        }
    }
}

@Composable
fun FlashCardItem(word: WordEntity) {

    var flipped by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .height(340.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { flipped = !flipped }
                )
            },

        colors = CardDefaults.cardColors(
            containerColor = if (flipped)
                Color(0xFF4F6F52)   // dark green
            else
                Color.White
        ),

        elevation = CardDefaults.cardElevation(10.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            // FRONT SIDE
            if (!flipped) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = word.word,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Tap to reveal meaning",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

            }
            // BACK SIDE
            else {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = word.meaning,
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = word.kannadaMeaning,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = word.category,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}