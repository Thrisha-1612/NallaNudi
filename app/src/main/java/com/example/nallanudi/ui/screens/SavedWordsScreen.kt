package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity

@Composable
fun SavedWordsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    val savedWords by dao.getSavedWords()
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7))
            .padding(16.dp)
    ) {

        Text(
            text = "Saved Words",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (savedWords.isEmpty()) {

            Text(
                text = "No saved words yet",
                color = Color.Gray
            )

        } else {

            LazyColumn {

                items(savedWords) { word: WordEntity ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                navController.navigate("word_detail/${word.word}")
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = word.word,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = word.kannadaMeaning,
                                color = Color.Blue
                            )
                        }
                    }
                }
            }
        }
    }
}