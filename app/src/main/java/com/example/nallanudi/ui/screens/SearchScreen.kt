package com.example.nallanudi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavController,
    query: String?
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val searchQuery = query ?: ""

    var results by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    LaunchedEffect(searchQuery) {

        coroutineScope.launch {

            val db = DatabaseInstance.getDatabase(context)

            results = db.wordDao().searchWords(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(text = "Search Results: $searchQuery")

        Spacer(modifier = Modifier.height(12.dp))

        if (results.isEmpty()) {

            Text("No results found")

        } else {

            LazyColumn {

                items(results) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("word_detail/${item.word}")
                            }
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(text = item.word)

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(text = item.kannadaMeaning)
                        }
                    }
                }
            }
        }
    }
}