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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SearchScreen(navController: NavController, query: String?) {

    val context = LocalContext.current

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    val searchQuery = query ?: ""

    // ✅ THIS IS THE CORRECT PLACE
    val results by dao.searchWords(searchQuery)
        .collectAsState(initial = emptyList())

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
                Column(Modifier.padding(16.dp)) {
                    Text(item.word)
                    Text(item.kannadaMeaning)
                }
            }
        }
    }
}