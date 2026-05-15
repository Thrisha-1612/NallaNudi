package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    name: String?,
    navController: NavController
) {

    val context = LocalContext.current
    val green = Color(0xFF0B5D3B)

    val dao = remember {
        DatabaseInstance.getDatabase(context).wordDao()
    }

    var words by remember { mutableStateOf<List<WordEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // ✅ FIXED FLOW COLLECTION
    LaunchedEffect(name) {

        isLoading = true

        if (!name.isNullOrBlank()) {

            dao.getWordsByCategory(name.trim()).collect { list ->
                words = list
            }

        } else {
            words = emptyList()
        }

        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = name ?: "Category",
                        fontWeight = FontWeight.Bold,
                        color = green
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = green
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
        ) {

            Text(
                text = "Learn meanings easily",
                fontSize = 13.sp,
                color = green,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = green)
                }

            } else if (words.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No words found in this category", color = Color.Gray)
                }

            } else {

                LazyColumn {

                    items(words) { word ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable {
                                    navController.navigate("word_detail/${word.word}")
                                }
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
                                    text = word.kannadaMeaning
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}