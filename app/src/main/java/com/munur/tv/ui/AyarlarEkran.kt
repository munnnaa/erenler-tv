package com.munur.tv.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.munur.tv.AnaViewModel

private fun String.maskele() = replace(Regex("(password=)[^&]+"), "\$1••••")

@Composable
fun AyarlarEkran(vm: AnaViewModel, onGeri: () -> Unit) {
    var listeler by remember { mutableStateOf(vm.depo.listeler()) }
    var yeni by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(56.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("M3U Listeleri", style = MaterialTheme.typography.headlineMedium)

        BasicTextField(
            value = yeni,
            onValueChange = { yeni = it },
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
            cursorBrush = SolidColor(Turkuaz),
            modifier = Modifier.fillMaxWidth()
                .border(2.dp, Turkuaz, RoundedCornerShape(10.dp))
                .padding(18.dp),
            decorationBox = { alan ->
                Box {
                    if (yeni.isEmpty()) Text("https://… M3U adresini yaz", color = Color.Gray)
                    alan()
                }
            }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                if (yeni.startsWith("http")) {
                    vm.depo.listeEkle(yeni); listeler = vm.depo.listeler(); yeni = ""
                }
            }) { Text("Ekle") }
            Button(onClick = onGeri) { Text("Kaydet ve dön") }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(listeler) { url ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(url.maskele(), Modifier.weight(1f), maxLines = 1)
                    Button(onClick = { vm.depo.listeSil(url); listeler = vm.depo.listeler() }) { Text("Sil") }
                }
            }
        }
    }
}
