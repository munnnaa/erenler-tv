package com.munur.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.darkColorScheme

// ERENLER TV — futuristik neon palet
val Lacivert = Color(0xFF05060A)        // zemin
val Turkuaz = Color(0xFF00F0FF)         // neon camgöbeği
val Magenta = Color(0xFFFF2BD6)         // neon pembe
val KartRengi = Color(0x99121826)       // yarı saydam cam kart
val CanliKirmizi = Color(0xFFFF2E4D)
val NeonGecis = Brush.linearGradient(listOf(Turkuaz, Magenta))

@Composable
fun ErenlerTema(icerik: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Turkuaz, onPrimary = Lacivert,
            background = Lacivert, surface = KartRengi, onSurface = Color.White
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            colors = SurfaceDefaults.colors(containerColor = Lacivert, contentColor = Color.White)
        ) {
            Box(
                Modifier.fillMaxSize()
                    .background(Brush.radialGradient(listOf(Turkuaz.copy(alpha = 0.18f), Color.Transparent), center = Offset(0f, 0f), radius = 1100f))
                    .background(Brush.radialGradient(listOf(Magenta.copy(alpha = 0.14f), Color.Transparent), center = Offset(2200f, 1300f), radius = 1300f))
            ) { icerik() }
        }
    }
}
