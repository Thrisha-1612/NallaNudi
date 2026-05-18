package com.example.nallanudi.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
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
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WordDetailScreen(
    navController: NavController,
    word: String
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    var wordData by remember {
        mutableStateOf<WordEntity?>(null)
    }

    // 🔊 TEXT TO SPEECH
    var tts by remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    LaunchedEffect(Unit) {

        tts = TextToSpeech(context) {

            if (it == TextToSpeech.SUCCESS) {

                tts?.language = Locale.US
            }
        }

        wordData = dao.getWord(word)
    }

    DisposableEffect(Unit) {

        onDispose {

            tts?.stop()
            tts?.shutdown()
        }
    }

    if (wordData == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator()
        }

        return
    }

    val data = wordData!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // WORD HEADER CARD
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF355C7D)
            ),

            shape = MaterialTheme.shapes.extraLarge,

            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                // 🔊 WORD + SPEAKER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = data.word,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = {

                            tts?.speak(
                                data.word,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                            )
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = data.kannadaMeaning,
                    fontSize = 22.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.medium
                ) {

                    Text(
                        text = data.category.uppercase(),
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // MEANING CARD
        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Meaning",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF355C7D)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = data.meaning,
                    fontSize = 18.sp,
                    lineHeight = 28.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SAVE BUTTON
        Button(
            onClick = {

                scope.launch {

                    if (data.isSaved) {

                        dao.unsaveWord(data.word)

                    } else {

                        dao.saveWord(data.word)
                    }

                    wordData = dao.getWord(word)
                }
            },

            modifier = Modifier.fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F6F52)
            ),

            shape = MaterialTheme.shapes.large
        ) {

            Icon(
                imageVector = if (data.isSaved)
                    Icons.Default.Bookmark
                else
                    Icons.Default.BookmarkBorder,

                contentDescription = null
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text =
                    if (data.isSaved)
                        "Saved"
                    else
                        "Save Word",

                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}