package com.example.nallanudi.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.SavedWordEntity
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun WordDetailScreen(
    navController: NavController,
    word: String?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var wordData by remember { mutableStateOf<WordEntity?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val darkGreen = Color(0xFF0B3D2E)
    val accentYellow = Color(0xFFFFD54F)

    // TTS INIT
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // LOAD DATA
    LaunchedEffect(word) {
        if (word.isNullOrBlank()) return@LaunchedEffect

        val db = DatabaseInstance.getDatabase(context)

        wordData = withContext(Dispatchers.IO) {
            db.wordDao().getWord(word)
        }

        isSaved = withContext(Dispatchers.IO) {
            db.wordDao().isWordSaved(word) != null
        }

        isLoading = false
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = darkGreen)
        }
        return
    }

    if (wordData == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Word not found")
        }
        return
    }

    val data = wordData!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkGreen)
                .padding(24.dp)
        ) {
            Column {

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Surface(
                    color = Color(0xFF1B4D3E),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = data.category.uppercase(),
                        color = accentYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = data.word,
                    fontSize = 32.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = data.kannadaMeaning,
                    fontSize = 22.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // BUTTONS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {
                    tts?.speak(
                        data.word,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE8F5E9)
                )
            ) {
                Icon(Icons.Default.VolumeUp, null, tint = Color(0xFF1B5E20))
                Spacer(Modifier.width(8.dp))
                Text("Listen", color = Color(0xFF1B5E20))
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val db = DatabaseInstance.getDatabase(context)

                        if (isSaved) {
                            db.wordDao().deleteWord(data.word)
                            isSaved = false
                        } else {
                            db.wordDao().saveWord(
                                SavedWordEntity(
                                    word = data.word,
                                    meaning = data.meaning,
                                    kannadaMeaning = data.kannadaMeaning,
                                    category = data.category
                                )
                            )
                            isSaved = true
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSaved) Color(0xFFFF5252) else Color(0xFF2E7D32)
                )
            ) {
                Icon(Icons.Default.BookmarkBorder, null)
                Spacer(Modifier.width(8.dp))
                Text(if (isSaved) "Saved" else "Save")
            }
        }

        Column(Modifier.padding(16.dp)) {

            SectionTitle("SIMPLE EXPLANATION")
            ContentCard(text = data.meaning)

            Spacer(Modifier.height(20.dp))

            SectionTitle("IN A SENTENCE")
            ContentCard(
                text = "\"${data.word} is a key concept in ${data.category}.\"",
                backgroundColor = Color(0xFFFDF7E7)
            )
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun ContentCard(
    text: String,
    backgroundColor: Color = Color.White
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.DarkGray
        )
    }
}
