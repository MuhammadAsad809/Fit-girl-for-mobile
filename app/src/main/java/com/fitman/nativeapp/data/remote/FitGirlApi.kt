package com.fitman.nativeapp.data.remote

import com.fitman.nativeapp.data.model.Game
import com.fitman.nativeapp.data.model.GameDetail
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * FitGirl Repacks scraper.
 * Mirrors the Telegram bot logic: search, latest, popular, and detail (magnet + mirrors).
 */
object FitGirlApi {
    private const val BASE = "https://fitgirl-repacks.site"
    private const val USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    private fun connect(url: String): Document {
        return Jsoup.connect(url)
            .userAgent(USER_AGENT)
            .timeout(20_000)
            .followRedirects(true)
            .ignoreHttpErrors(true)
            .get()
    }

    /** Search FitGirl by game name. Returns up to [limit] results. */
    fun search(query: String, limit: Int = 20): List<Game> {
        val q = query.trim()
        if (q.isEmpty()) return emptyList()
        val encoded = URLEncoder.encode(q, "UTF-8")
        val doc = connect("$BASE/?s=$encoded")
        return parseArticleList(doc, limit, tag = "FitGirl")
    }

    /** Latest repacks from homepage. */
    fun latest(limit: Int = 15): List<Game> {
        val doc = connect("$BASE/")
        return parseArticleList(doc, limit, tag = "Latest")
    }

    /** Top 50 of the month. */
    fun topOfMonth(limit: Int = 50): List<Game> {
        return parsePopularPage("$BASE/pop-repacks/", "Top Month", limit)
    }

    /** Top 150 of the year. */
    fun topOfYear(limit: Int = 150): List<Game> {
        return parsePopularPage("$BASE/popular-repacks-of-the-year/", "Top Year", limit)
    }

    private fun parseArticleList(doc: Document, limit: Int, tag: String): List<Game> {
        val list = mutableListOf<Game>()
        val articles = doc.select("article.post")
        for ((index, article) in articles.withIndex()) {
            if (list.size >= limit) break
            val titleTag = article.selectFirst("h1.entry-title, h2.entry-title") ?: continue
            val linkTag = titleTag.selectFirst("a") ?: continue
            val title = titleTag.text().trim()
            val href = linkTag.attr("abs:href").ifBlank { linkTag.attr("href") }
            if (title.isBlank() || href.isBlank()) continue

            val img = article.selectFirst("img")?.let {
                it.attr("abs:src").ifBlank { it.attr("abs:data-src") }
            }.orEmpty()

            val id = stableId(href)
            list.add(
                Game(
                    id = id,
                    title = title,
                    image = img,
                    price = "Repack",
                    url = href,
                    tag = tag
                )
            )
        }
        return list
    }

    private fun parsePopularPage(url: String, tag: String, limit: Int): List<Game> {
        val doc = connect(url)
        val list = mutableListOf<Game>()
        val content = doc.selectFirst("div.entry-content") ?: return emptyList()
        for (a in content.select("a[href]")) {
            if (list.size >= limit) break
            val href = a.attr("abs:href").ifBlank { a.attr("href") }
            if (!href.contains("fitgirl-repacks.site")) continue
            if (href.endsWith("pop-repacks/") || href.endsWith("popular-repacks-of-the-year/")) continue

            var title = a.text().trim()
            if (title.isBlank() || title.length <= 3) {
                title = href.trimEnd('/').substringAfterLast('/').replace('-', ' ')
                    .replaceFirstChar { it.uppercase() }
            }
            if (title.length <= 3) continue

            val id = stableId(href)
            list.add(
                Game(
                    id = id,
                    title = title,
                    image = "",
                    price = "Repack",
                    url = href,
                    tag = tag
                )
            )
        }
        return list
    }

    /**
     * Fetch full detail for a FitGirl post URL.
     * Magnet, mirrors (Pixeldrain, Gofile, Qiwi, etc.), cover image, description.
     */
    fun detail(url: String): FitGirlDetail? {
        if (url.isBlank()) return null
        return try {
            val doc = connect(url)
            val content = doc.selectFirst("div.entry-content") ?: doc.body()

            val title = doc.selectFirst("h1.entry-title, h2.entry-title")?.text()?.trim()
                ?: doc.title().substringBefore("|").trim()

            var imageUrl = ""
            content.selectFirst("img")?.let { img ->
                imageUrl = img.attr("abs:src").ifBlank { img.attr("abs:data-src") }
            }

            var magnet: String? = null
            val magnets = mutableListOf<MirrorLink>()
            val mirrors = mutableListOf<MirrorLink>()
            var magnetIndex = 0

            for (a in content.select("a[href]")) {
                val href = a.attr("abs:href").ifBlank { a.attr("href") }
                val linkText = a.text().trim()

                when {
                    href.startsWith("magnet:") -> {
                        if (magnet == null) magnet = href
                        magnetIndex += 1
                        var name = linkText.ifBlank { "Part $magnetIndex" }
                        if (name.length > 48) name = "Part $magnetIndex"
                        if (name.equals("magnet", true) || name.startsWith("magnet:")) {
                            name = "Part $magnetIndex"
                        }
                        // Prefer labels like "Part 1", "Magnet", "Download"
                        if (magnets.none { it.url == href }) {
                            magnets.add(MirrorLink(name = name, url = href))
                        }
                    }
                    isMirror(href) -> {
                        if (mirrors.none { it.url == href }) {
                            var name = linkText.ifBlank { domainName(href) }
                            if (name.lowercase().contains("filehoster:")) {
                                name = name.substringAfter(":").trim()
                            } else if (name.lowercase().contains("torrent file only")) {
                                name = "Torrent Mirror"
                            }
                            mirrors.add(MirrorLink(name = name.ifBlank { domainName(href) }, url = href))
                        }
                    }
                }
            }

            val description = content.select("p").take(6).joinToString("\n\n") { it.text().trim() }
                .take(2000)

            val infoHash = magnet?.let { extractInfoHash(it) }

            FitGirlDetail(
                id = stableId(url),
                title = title.ifBlank { "Unknown" },
                url = url,
                image = imageUrl,
                description = description,
                magnet = magnet,
                infoHash = infoHash,
                magnets = magnets,
                mirrors = mirrors
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun isMirror(href: String): Boolean {
        val h = href.lowercase()
        return listOf(
            "pixeldrain.com", "gofile.io", "qiwi.gg", "buzzheavier.com",
            "fuckingfast", "multiupload", "multiup.org", "paste.fitgirl-repacks.site",
            "mega.nz", "mediafire.com", "1fichier.com"
        ).any { h.contains(it) }
    }

    private fun domainName(url: String): String {
        return try {
            java.net.URI(url).host?.removePrefix("www.") ?: "Mirror"
        } catch (_: Exception) {
            "Mirror"
        }
    }

    fun extractInfoHash(magnet: String): String? {
        val hex = Pattern.compile("urn:btih:([a-fA-F0-9]{40})", Pattern.CASE_INSENSITIVE)
            .matcher(magnet)
        if (hex.find()) return hex.group(1)?.uppercase()
        val b32 = Pattern.compile("urn:btih:([a-zA-Z2-7]{32})", Pattern.CASE_INSENSITIVE)
            .matcher(magnet)
        if (b32.find()) return b32.group(1)?.uppercase()
        return null
    }

    /** Stable positive id from URL for list keys / favorites. */
    private fun stableId(url: String): Int {
        val slug = url.trimEnd('/').substringAfterLast('/')
        return (slug.hashCode() and 0x7FFFFFFF).let { if (it == 0) 1 else it }
    }

    /**
     * Match a Steam-style title to a FitGirl repack detail (magnet + mirrors).
     */
    fun findRepackForTitle(steamTitle: String): FitGirlDetail? {
        val q = steamTitle
            .replace(Regex("""\s*[\(\[][^\)\]]*[\)\]]"""), " ")
            .replace(Regex("""\b(GOTY|Edition|Deluxe|Ultimate|Remastered|Remake|HD|Complete)\b""", RegexOption.IGNORE_CASE), " ")
            .replace(Regex("""\s+"""), " ")
            .trim()
        if (q.length < 2) return null
        val results = search(q, limit = 8)
        if (results.isEmpty()) return null
        val qLower = q.lowercase()
        val best = results.maxByOrNull { g ->
            val t = g.title.lowercase()
            when {
                t.startsWith(qLower) -> 100
                t.contains(qLower) -> 80
                else -> qLower.split(" ").count { w -> w.length > 2 && t.contains(w) } * 10
            }
        } ?: results.first()
        return detail(best.url)
    }
}


data class MirrorLink(
    val name: String,
    val url: String
)

data class FitGirlDetail(
    val id: Int,
    val title: String,
    val url: String,
    val image: String = "",
    val description: String = "",
    val magnet: String? = null,
    val infoHash: String? = null,
    val magnets: List<MirrorLink> = emptyList(),
    val mirrors: List<MirrorLink> = emptyList()
) {
    /** All torrent/magnet parts for direct open (Part 1, Part 2, Full, ...). */
    val torrentParts: List<MirrorLink>
        get() = when {
            magnets.isNotEmpty() -> magnets
            !magnet.isNullOrBlank() -> listOf(MirrorLink("Full Game (Torrent)", magnet!!))
            else -> emptyList()
        }

    fun toGameDetail(): GameDetail {
        return GameDetail(
            id = id,
            title = title,
            image = image,
            description = description,
            developer = "FitGirl",
            publisher = "FitGirl Repacks",
            price = "Repack",
            url = url,
            releaseDate = "",
            reviews = if (magnet != null || magnets.isNotEmpty()) "Magnet available" else "",
            genres = listOf("Repack"),
            screenshots = emptyList()
        )
    }
}
