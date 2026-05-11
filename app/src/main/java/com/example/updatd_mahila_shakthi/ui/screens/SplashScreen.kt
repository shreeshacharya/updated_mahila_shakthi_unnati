package com.example.updatd_mahila_shakthi.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.updatd_mahila_shakthi.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        delay(1200)
        onNavigate()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(scale.value)) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = com.example.updatd_mahila_shakthi.R.drawable.mahila_shakti_logo),
                contentDescription = "Mahila Shakti Logo",
                modifier = Modifier.size(240.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "mahila shakthi", 
                style = MaterialTheme.typography.displaySmall, 
                color = Color(0xFF4A148C),
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.width(40.dp), color = Color(0xFFE65100), thickness = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "unnati", 
                    style = MaterialTheme.typography.titleLarge, 
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(modifier = Modifier.width(40.dp), color = Color(0xFFE65100), thickness = 2.dp)
            }
        }
    }
}
