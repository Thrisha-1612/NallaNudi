package com.example.nallanudi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
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
import com.example.nallanudi.data.SavedWordEntity

@Composable
fun FlashcardScreen() {

    val context = LocalContext.current

    // LIVE DATA (ROOM FLOW → STATE)
    val words by DatabaseInstance.getDatabase(context)
        .wordDao()
        .getSavedWords()
        .collectAsState(initial = emptyList())

    var index by remember { mutableStateOf(0) }
    var flipped by remember { mutableStateOf(false) }

    if (words.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No saved words yet")
        }
        return
    }

    // FIX: prevent crash when list changes
    val safeIndex = index % words.size
    val currentWord = words[safeIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(words.size) {
                detectHorizontalDragGestures { _, dragAmount ->

                    if (dragAmount > 20) {
                        index = if (index - 1 < 0) words.size - 1 else index - 1
                        flipped = false
                    }

                    if (dragAmount < -20) {
                        index = (index + 1) % words.size
                        flipped = false
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(300.dp)
                    .clickable { flipped = !flipped },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (!flipped) {

                        Text(
                            text = currentWord.word,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B3D2E)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Tap to reveal meaning", color = Color.Gray)

                    } else {

                        Text(
                            text = currentWord.kannadaMeaning,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentWord.meaning,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}