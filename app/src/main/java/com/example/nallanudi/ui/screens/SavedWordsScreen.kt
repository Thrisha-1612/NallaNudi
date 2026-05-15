package com.example.nallanudi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.SavedWordEntity

@Composable
fun SavedWordsScreen(navController: NavController) {

    val context = LocalContext.current

    // FIXED: Flow → State correctly
    val savedWords by remember {
        DatabaseInstance.getDatabase(context)
            .wordDao()
            .getSavedWords()
    }.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Saved Words",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (savedWords.isEmpty()) {

            Text("No saved words yet 📘")

        } else {

            LazyColumn {

                items(savedWords) { word ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("word_detail/${word.word}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(word.word, style = MaterialTheme.typography.titleLarge)
                            Text(word.kannadaMeaning)
                            Text(word.category)
                        }
                    }
                }
            }
        }
    }
}