package com.example.nallanudi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val coroutineScope = rememberCoroutineScope()

    var wordData by remember {
        mutableStateOf<WordEntity?>(null)
    }

    LaunchedEffect(Unit) {

        val db = DatabaseInstance.getDatabase(context)

        wordData = db.wordDao().getWord(word)
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF8FAFC),
            Color(0xFFEFF4FF)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        if (wordData == null) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    color = Color(0xFF355C7D)
                )
            }

        } else {

            val item = wordData!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Technical Dictionary",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(32.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF355C7D)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(28.dp)
                    ) {

                        Text(
                            text = item.word,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = item.kannadaMeaning,
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        ) {

                            Text(
                                text = item.category,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Meaning",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(24.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {

                    Text(
                        text = item.meaning,
                        modifier = Modifier.padding(22.dp),
                        fontSize = 17.sp,
                        lineHeight = 28.sp,
                        color = Color(0xFF334155)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(

                    onClick = {

                        coroutineScope.launch {

                            val db =
                                DatabaseInstance.getDatabase(context)

                            if (item.isSaved) {

                                db.wordDao().unsaveWord(item.word)

                            } else {

                                db.wordDao().saveWord(item.word)
                            }

                            wordData =
                                db.wordDao().getWord(item.word)
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    shape = RoundedCornerShape(20.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355C7D)
                    )
                ) {

                    Icon(
                        imageVector =
                            if (item.isSaved)
                                Icons.Filled.Bookmark
                            else
                                Icons.Outlined.BookmarkBorder,

                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text =
                            if (item.isSaved)
                                "Saved to Flashcards"
                            else
                                "Save to Flashcards",

                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}