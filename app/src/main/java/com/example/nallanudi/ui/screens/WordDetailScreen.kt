package com.example.nallanudi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.launch

@Composable
fun WordDetailScreen(
    navController: NavController,
    word: String
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember {
        DatabaseInstance.getDatabase(context)
    }

    var wordData by remember { mutableStateOf<WordEntity?>(null) }
    var isSaved by remember { mutableStateOf(false) }

    // Load word from DB
    LaunchedEffect(word) {
        wordData = db.wordDao().getWord(word)

        wordData?.let {
            isSaved = it.isSaved
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        wordData?.let { data ->

            Text(
                text = data.word,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = data.meaning,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = data.kannadaMeaning,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    scope.launch {

                        if (isSaved) {
                            db.wordDao().unsaveWord(data.word)
                            isSaved = false
                        } else {
                            db.wordDao().saveWord(data.word)
                            isSaved = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSaved) Color.Red else Color(0xFF1B5E20)
                )
            ) {

                Text(
                    if (isSaved) "Remove from Saved" else "Save Word"
                )
            }
        }
    }
}