package com.munur.tv.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

/** M3U listelerini cihazda saklar ve indirir. Yeni kaynak türleri (ör. Jellyfin) buraya eklenebilir. */
class KanalDeposu(context: Context) {
    private val prefs = context.getSharedPreferences("ayarlar", Context.MODE_PRIVATE)
    private val http = OkHttpClient.Builder().followRedirects(true).build()

    fun listeler(): List<String> = prefs.getStringSet("listeler", emptySet()).orEmpty().sorted()

    fun listeEkle(url: String) =
        prefs.edit().putStringSet("listeler", listeler().toSet() + url.trim()).apply()

    fun listeSil(url: String) =
        prefs.edit().putStringSet("listeler", listeler().toSet() - url).apply()

    suspend fun kanallar(): List<Kanal> = withContext(Dispatchers.IO) {
        listeler().flatMap { url ->
            runCatching {
                val istek = Request.Builder().url(url).header("User-Agent", "ErenlerTV/1.0").build()
                http.newCall(istek).execute().use { it.body?.string().orEmpty() }
            }.map(M3uParser::parse).getOrDefault(emptyList())
        }
    }
}
