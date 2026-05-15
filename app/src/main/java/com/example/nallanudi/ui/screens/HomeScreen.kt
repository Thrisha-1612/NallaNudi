package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CategoryItem(
    val name: String,
    val color: Color,
    val icon: String
)

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var searchText by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<WordEntity>>(emptyList()) }
    var showSuggestions by remember { mutableStateOf(false) }

    val darkGreen = Color(0xFF0B3D2E)
    val lightYellow = Color(0xFFFDF7E7)
    val accentYellow = Color(0xFFFFD54F)

    val categories = listOf(
        CategoryItem("Science", Color(0xFFE8F5E9), "🔬"),
        CategoryItem("Math", Color(0xFFFFF3E0), "📐"),
        CategoryItem("Commerce", Color(0xFFE3F2FD), "📊"),
        CategoryItem("Physics", Color(0xFFF3E5F5), "⚛️"),
        CategoryItem("Chemistry", Color(0xFFE0F2F1), "🧪"),
        CategoryItem("Biology", Color(0xFFFFF1F1), "🧬")
    )

    fun loadSuggestions(query: String) {

        if (query.trim().length < 2) {
            suggestions = emptyList()
            showSuggestions = false
            return
        }

        coroutineScope.launch {

            val db = DatabaseInstance.getDatabase(context)

            val results: List<WordEntity> = withContext(Dispatchers.IO) {
                db.wordDao().searchWords(query.trim())
            }

            suggestions = results.toList()
            showSuggestions = suggestions.isNotEmpty()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            lightYellow,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🌿", fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {

                    Text(
                        "NALLA",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )

                    Text(
                        "NUDI",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        lightYellow,
                        shape = CircleShape
                    )
                    .clickable {
                        navController.navigate("saved_words")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("🎗️", fontSize = 22.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "ನಮಸ್ಕಾರ,",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Let's learn something new.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box {

            OutlinedTextField(
                value = searchText,

                onValueChange = {
                    searchText = it
                    loadSuggestions(it)
                },

                placeholder = {
                    Text("Search STEM terms...")
                },

                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),

                keyboardActions = KeyboardActions(
                    onSearch = {

                        if (searchText.isNotBlank()) {

                            showSuggestions = false

                            navController.navigate(
                                "word_detail/$searchText"
                            )
                        }
                    }
                ),

                singleLine = true
            )

            if (showSuggestions) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 62.dp),

                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column {

                        suggestions.forEach { word ->

                            ListItem(
                                headlineContent = {
                                    Text(word.word)
                                },

                                supportingContent = {
                                    Text(word.kannadaMeaning)
                                },

                                modifier = Modifier.clickable {

                                    showSuggestions = false
                                    searchText = ""

                                    navController.navigate(
                                        "word_detail/${word.word}"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clickable {
                    navController.navigate("word_detail/Photosynthesis")
                },

            shape = RoundedCornerShape(32.dp),

            colors = CardDefaults.cardColors(
                containerColor = darkGreen
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {

                Text(
                    "WORD OF THE DAY",
                    color = accentYellow
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    "Photosynthesis",
                    fontSize = 30.sp,
                    color = Color.White
                )

                Text(
                    "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ",
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Categories",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),

            verticalArrangement = Arrangement.spacedBy(16.dp),

            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(categories) { item ->

                Card(
                    modifier = Modifier
                        .aspectRatio(0.85f)
                        .clickable {
                            navController.navigate(
                                "category/${item.name}"
                            )
                        },

                    shape = RoundedCornerShape(24.dp)
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),

                        horizontalAlignment = Alignment.CenterHorizontally,

                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(item.icon)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(item.name)
                    }
                }
            }
        }
    }
}