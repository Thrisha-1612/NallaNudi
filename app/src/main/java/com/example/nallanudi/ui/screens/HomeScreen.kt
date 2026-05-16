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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nallanudi.data.DatabaseInstance
import com.example.nallanudi.data.WordEntity
import kotlinx.coroutines.launch

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

    var suggestions by remember {
        mutableStateOf<List<WordEntity>>(emptyList())
    }

    var showSuggestions by remember {
        mutableStateOf(false)
    }

    // ✅ NEW WORD OF DAY STATE
    var wordOfDay by remember {
        mutableStateOf<WordEntity?>(null)
    }

    val lightYellow = Color(0xFFFDF7E7)

    val categories = listOf(

        CategoryItem(
            "Science",
            Color(0xFF355C7D),
            "🔬"
        ),

        CategoryItem(
            "Math",
            Color(0xFF6C5B7B),
            "📐"
        ),

        CategoryItem(
            "Commerce",
            Color(0xFF4E6E58),
            "📊"
        ),

        CategoryItem(
            "Physics",
            Color(0xFF5C6B73),
            "⚛️"
        ),

        CategoryItem(
            "Chemistry",
            Color(0xFF7B5E57),
            "🧪"
        ),

        CategoryItem(
            "Biology",
            Color(0xFF4F6F52),
            "🧬"
        )
    )

    fun loadSuggestions(query: String) {

        if (query.trim().length < 2) {

            suggestions = emptyList()
            showSuggestions = false
            return
        }

        coroutineScope.launch {

            val db = DatabaseInstance.getDatabase(context)

            db.wordDao()
                .searchWords(query.trim())
                .collect {

                    suggestions = it
                    showSuggestions = it.isNotEmpty()
                }
        }
    }

    // ✅ WORD OF DAY LOADER
    LaunchedEffect(Unit) {

        val db = DatabaseInstance.getDatabase(context)

        db.wordDao()
            .getAllWords()
            .collect { words ->

                if (words.isNotEmpty()) {

                    wordOfDay = words.random()
                }
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

                    Text(
                        "🌿",
                        fontSize = 22.sp
                    )
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

                Text(
                    "🎗️",
                    fontSize = 22.sp
                )
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
                                "search/$searchText"
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

        // ✅ PREMIUM WORD OF DAY CARD
        Card(

            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clickable {

                    wordOfDay?.let {

                        navController.navigate(
                            "word_detail/${it.word}"
                        )
                    }
                },

            shape = RoundedCornerShape(36.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {

            Box(

                modifier = Modifier
                    .fillMaxSize()
                    .background(

                        brush = Brush.linearGradient(

                            colors = listOf(
                                Color(0xFF355C7D),
                                Color(0xFF4F6F52)
                            )
                        )
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp)
                ) {

                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    ) {

                        Text(
                            text = "WORD OF THE DAY",

                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            ),

                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = wordOfDay?.word
                            ?: "Photosynthesis",

                        fontSize = 34.sp,

                        color = Color.White,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = wordOfDay?.kannadaMeaning
                            ?: "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ",

                        color = Color.White.copy(alpha = 0.92f),

                        fontSize = 24.sp,

                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = wordOfDay?.meaning
                            ?: "Process by which plants make food",

                        color = Color.White.copy(alpha = 0.82f),

                        lineHeight = 24.sp,

                        fontSize = 15.sp
                    )
                }
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
                        .aspectRatio(0.9f)
                        .clickable {

                            navController.navigate(
                                "category/${item.name.lowercase()}"
                            )
                        },

                    shape = RoundedCornerShape(26.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = item.color
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {

                    Column(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),

                        horizontalAlignment = Alignment.CenterHorizontally,

                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = item.icon,
                            fontSize = 34.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = item.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}