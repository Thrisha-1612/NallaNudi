package com.example.nallanudi.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    var startAnimation by remember {
        mutableStateOf(false)
    }

    val scaleAnim by animateFloatAsState(
        targetValue =
            if (startAnimation) 1f else 0.7f,

        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),

        label = ""
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF355C7D),
            Color(0xFF4F6F52)
        )
    )

    LaunchedEffect(Unit) {

        startAnimation = true

        delay(2200)

        navController.navigate("home") {

            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scaleAnim)
        ) {

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(
                        Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "🌿",
                    fontSize = 52.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "NALLA NUDI",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Kannada Technical Dictionary",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 16.sp
            )
        }
    }
}