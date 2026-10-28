package com.munur.tv

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.munur.tv.data.Kanal
import com.munur.tv.data.KanalDeposu
import kotlinx.coroutines.launch

class AnaViewModel(app: Application) : AndroidViewModel(app) {
    val depo = KanalDeposu(app)
    var kanallar by mutableStateOf<List<Kanal>>(emptyList()); private set
    var yukleniyor by mutableStateOf(false); private set

    init { yenile() }

    fun yenile() {
        viewModelScope.launch {
            yukleniyor = true
            kanallar = depo.kanallar()
            yukleniyor = false
        }
    }
}
