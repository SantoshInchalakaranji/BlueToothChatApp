package com.prplmnstr.bluetoothchat.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RadarDetectorAnimation() {
    val color = MaterialTheme.colorScheme.inversePrimary
    val infiniteTransition = rememberInfiniteTransition()
    val radius = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        drawCircle(
            color = color,
            center = center,
            radius = radius.value,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Preview
@Composable
fun PreviewRadarDetectorAnimation() {

        RadarDetectorAnimation()

}
