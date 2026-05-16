package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SavedWordsScreen(navController: NavController) {

    val context = LocalContext.current

    var savedWords by remember { mutableStateOf<List<WordEntity>>(emptyList()) }

    val db = remember {
        DatabaseInstance.getDatabase(context)
    }

    // 🔥 FLOW COLLECT (correct way)
    LaunchedEffect(Unit) {
        db.wordDao()
            .getSavedWords()
            .collectLatest {
                savedWords = it
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {

        Text(
            text = "Saved Words",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (savedWords.isEmpty()) {
            Text("No saved words yet")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(savedWords) { word ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("word_detail/${word.word}")
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = word.word,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = word.kannadaMeaning)
                    }
                }
            }
        }
    }
}