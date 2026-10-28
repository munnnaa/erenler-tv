package com.munur.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.munur.tv.data.Kanal
import com.munur.tv.ui.*

sealed interface Ekran {
    data object Ana : Ekran
    data object Ayarlar : Ekran
    data class Oynat(val kanal: Kanal) : Ekran
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ErenlerTema { Uygulama() } }
    }
}

@Composable
fun Uygulama(vm: AnaViewModel = viewModel()) {
    var ekran by remember { mutableStateOf<Ekran>(Ekran.Ana) }
    when (val e = ekran) {
        Ekran.Ana -> AnaEkran(vm, onKanal = { ekran = Ekran.Oynat(it) }, onAyarlar = { ekran = Ekran.Ayarlar })
        Ekran.Ayarlar -> {
            val don = { ekran = Ekran.Ana; vm.yenile() }
            BackHandler(onBack = don)
            AyarlarEkran(vm, onGeri = don)
        }
        is Ekran.Oynat -> {
            BackHandler { ekran = Ekran.Ana }
            OynaticiEkran(e.kanal)
        }
    }
}
