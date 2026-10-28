package com.munur.tv.data

data class Kanal(val ad: String, val url: String, val logo: String?, val grup: String)

object M3uParser {
    private fun attr(satir: String, anahtar: String): String? =
        Regex("$anahtar=\"([^\"]*)\"").find(satir)?.groupValues?.get(1)?.takeIf { it.isNotBlank() }

    fun parse(m3u: String): List<Kanal> {
        val liste = mutableListOf<Kanal>()
        var ad: String? = null
        var logo: String? = null
        var grup = "Diğer"
        m3u.lineSequence().map { it.trim() }.forEach { satir ->
            when {
                satir.startsWith("#EXTINF") -> {
                    ad = (if ("\"," in satir) satir.substringAfterLast("\",") else satir.substringAfterLast(",")).trim()
                    logo = attr(satir, "tvg-logo")
                    grup = attr(satir, "group-title") ?: "Diğer"
                }
                satir.isNotEmpty() && !satir.startsWith("#") -> {
                    liste.add(Kanal(ad ?: satir, satir, logo, grup))
                    ad = null; logo = null; grup = "Diğer"
                }
            }
        }
        return liste
    }
}
