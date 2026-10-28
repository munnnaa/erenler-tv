package com.munur.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.munur.tv.data.Kanal

@Composable
fun OynaticiEkran(kanal: Kanal) {
    val context = LocalContext.current
    var hata by remember { mutableStateOf<String?>(null) }

    val oynatici = remember(kanal.url) {
        ExoPlayer.Builder(context).build().apply {
            val oge = MediaItem.Builder().setUri(kanal.url).apply {
                if (kanal.url.contains(".m3u8")) setMimeType(MimeTypes.APPLICATION_M3U8)
            }.build()
            setMediaItem(oge)
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    hata = "Yayın açılamadı: ${error.errorCodeName}"
                }
            })
            prepare()
            playWhenReady = true
        }
    }
    DisposableEffect(oynatici) { onDispose { oynatici.release() } }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { PlayerView(it).apply { player = oynatici; keepScreenOn = true } },
            modifier = Modifier.fillMaxSize()
        )
        hata?.let { Text(it, Modifier.align(Alignment.Center), style = MaterialTheme.typography.headlineSmall) }
    }
}
