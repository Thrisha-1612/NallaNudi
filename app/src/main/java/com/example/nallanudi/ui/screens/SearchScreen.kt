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
import com.example.nallanudi.data.WordEntity

@Composable
fun SearchScreen(
    navController: NavController,
    query: String?
) {

    val context = LocalContext.current
    val searchQuery = query ?: ""

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    // ✅ FIXED: stable Flow collection
    val results by remember(searchQuery) {
        dao.searchWords(searchQuery)
    }.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(text = "Search Results: $searchQuery")

        Spacer(modifier = Modifier.height(12.dp))

        if (results.isEmpty()) {
            Text("No results found")
            return
        }

        LazyColumn {

            items(results) { item: WordEntity ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("word_detail/${item.word}")
                        }
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(text = item.word)
                        Text(text = item.kannadaMeaning)
                    }
                }
            }
        }
    }
}