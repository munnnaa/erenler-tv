package com.munur.tv.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.*
import coil.compose.AsyncImage
import com.munur.tv.AnaViewModel
import com.munur.tv.R
import com.munur.tv.data.Kanal
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Kose = RoundedCornerShape(16.dp)

@Composable
fun AnaEkran(vm: AnaViewModel, onKanal: (Kanal) -> Unit, onAyarlar: () -> Unit) {
    val gruplar = remember(vm.kanallar) { vm.kanallar.groupBy { it.grup } }

    Row(Modifier.fillMaxSize()) {
        // Yüzen cam menü
        Column(
            Modifier.padding(start = 20.dp, top = 120.dp, bottom = 120.dp).width(76.dp).fillMaxHeight()
                .background(KartRengi, RoundedCornerShape(38.dp))
                .border(1.dp, NeonGecis, RoundedCornerShape(38.dp))
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            NeonIkon("⟳") { vm.yenile() }
            NeonIkon("⚙", onAyarlar)
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 32.dp, top = 32.dp, bottom = 48.dp, end = 48.dp),
            verticalArrangement = Arrangement.spacedBy(34.dp)
        ) {
            item { UstBar() }
            when {
                vm.yukleniyor -> item { Text("SİNYAL ARANIYOR…", style = MaterialTheme.typography.headlineSmall, color = Turkuaz) }
                vm.kanallar.isEmpty() -> item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Henüz kanal yok", style = MaterialTheme.typography.headlineMedium)
                        Text("Ayarlar'dan M3U liste adresini ekle.")
                        Button(onClick = onAyarlar) { Text("AYARLARA GİT") }
                    }
                }
                else -> {
                    item { OneCikan(vm.kanallar.first(), onKanal) }
                    gruplar.forEach { (grup, liste) ->
                        item(key = grup) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(grup.uppercase(), style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 3.sp, fontWeight = FontWeight.Bold))
                                Box(Modifier.width(56.dp).height(2.dp).background(NeonGecis))
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(22.dp)
                                ) {
                                    items(liste, key = { it.url }) { k -> KanalKart(k) { onKanal(k) } }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UstBar() {
    var saat by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val bicim = SimpleDateFormat("HH:mm", Locale("tr"))
        while (true) { saat = bicim.format(Date()); delay(15_000) }
    }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.logo), contentDescription = "ERENLER TV", Modifier.size(52.dp).background(Lacivert, RoundedCornerShape(12.dp)))
        Spacer(Modifier.width(14.dp))
        Text(
            "ERENLER TV",
            style = TextStyle(brush = NeonGecis, fontSize = 30.sp, fontWeight = FontWeight.Black, letterSpacing = 6.sp)
        )
        Spacer(Modifier.weight(1f))
        Text(saat, style = TextStyle(color = Turkuaz, fontSize = 22.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Light))
    }
}

@Composable
private fun NeonIkon(simge: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(24.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.Transparent, contentColor = Color.White,
            focusedContainerColor = Turkuaz.copy(alpha = 0.2f), focusedContentColor = Turkuaz
        ),
        glow = ClickableSurfaceDefaults.glow(focusedGlow = Glow(Turkuaz, 10.dp))
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(simge, fontSize = 22.sp) }
    }
}

@Composable
private fun OneCikan(kanal: Kanal, onKanal: (Kanal) -> Unit) {
    Box(
        Modifier.fillMaxWidth().height(280.dp)
            .background(Brush.horizontalGradient(listOf(Color(0xCC0E1424), Color(0x330E1424))), RoundedCornerShape(24.dp))
            .border(1.dp, NeonGecis, RoundedCornerShape(24.dp))
            .padding(40.dp)
    ) {
        AsyncImage(
            model = kanal.logo, contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd).size(190.dp),
            contentScale = ContentScale.Fit
        )
        Column(Modifier.align(Alignment.CenterStart), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            CanliRozet()
            Text(kanal.ad, style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold), maxLines = 1)
            Text(kanal.grup.uppercase(), color = Turkuaz.copy(alpha = 0.8f), letterSpacing = 3.sp)
            Button(
                onClick = { onKanal(kanal) },
                colors = ButtonDefaults.colors(
                    containerColor = Turkuaz, contentColor = Lacivert,
                    focusedContainerColor = Magenta, focusedContentColor = Color.White
                ),
                glow = ButtonDefaults.glow(focusedGlow = Glow(Magenta, 14.dp))
            ) { Text("▶  İZLE", fontWeight = FontWeight.Bold, letterSpacing = 2.sp) }
        }
    }
}

@Composable
fun KanalKart(k: Kanal, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(240.dp).aspectRatio(16f / 9f),
        shape = CardDefaults.shape(Kose),
        scale = CardDefaults.scale(focusedScale = 1.12f),
        border = CardDefaults.border(
            border = Border(BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)), shape = Kose),
            focusedBorder = Border(BorderStroke(3.dp, NeonGecis), shape = Kose)
        ),
        glow = CardDefaults.glow(focusedGlow = Glow(Turkuaz, 16.dp)),
        colors = CardDefaults.colors(containerColor = KartRengi)
    ) {
        Box(Modifier.fillMaxSize()) {
            if (k.logo != null) {
                AsyncImage(
                    model = k.logo, contentDescription = k.ad,
                    modifier = Modifier.align(Alignment.Center).fillMaxSize(0.5f),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    k.ad.take(2).uppercase(), Modifier.align(Alignment.Center),
                    style = TextStyle(brush = NeonGecis, fontSize = 36.sp, fontWeight = FontWeight.Black)
                )
            }
            CanliRozet(Modifier.align(Alignment.TopStart).padding(10.dp))
            Text(
                k.ad,
                Modifier.align(Alignment.BottomStart).fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun CanliRozet(modifier: Modifier = Modifier) {
    Row(
        modifier.background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
            .border(1.dp, CanliKirmizi, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(Modifier.size(7.dp).background(CanliKirmizi, RoundedCornerShape(50)))
        Text("CANLI", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp), color = Color.White)
    }
}
