package com.example.updatd_mahila_shakthi.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    var targetValue by remember { mutableIntStateOf(0) }
    val animatedValue by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "counter"
    )

    LaunchedEffect(value) {
        targetValue = value.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradientColors))
                .padding(16.dp)
        ) {
            Column {
                Icon(
                    icon, contentDescription = title,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (value.startsWith("₹")) "₹$animatedValue" else animatedValue.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}
