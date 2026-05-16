package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@Composable
fun FlashcardScreen(
    navController: NavController
) {

    val context = LocalContext.current

    var savedWords by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    var currentIndex by remember {
        mutableStateOf(0)
    }

    var showMeaning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        val db = DatabaseInstance.getDatabase(context)

        savedWords = withContext(Dispatchers.IO) {
            db.wordDao().getSavedWords().first()
        }
    }

    if (savedWords.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "No Saved Words",
                fontSize = 20.sp
            )
        }

        return
    }

    val currentWord = savedWords[currentIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .pointerInput(currentIndex) {

                    detectHorizontalDragGestures { _, dragAmount ->

                        if (dragAmount < -50) {

                            if (currentIndex < savedWords.lastIndex) {
                                currentIndex++
                                showMeaning = false
                            }

                        } else if (dragAmount > 50) {

                            if (currentIndex > 0) {
                                currentIndex--
                                showMeaning = false
                            }
                        }
                    }
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
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
                    text = currentWord.category,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = currentWord.word,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (showMeaning) {

                    Text(
                        text = currentWord.kannadaMeaning,
                        fontSize = 24.sp,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentWord.meaning,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        showMeaning = !showMeaning
                    }
                ) {

                    Text(
                        if (showMeaning)
                            "Hide Meaning"
                        else
                            "Show Meaning"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "${currentIndex + 1} / ${savedWords.size}",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Swipe left or right",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}