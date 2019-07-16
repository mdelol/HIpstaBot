package com.home.hipstabot.matching.yandexmusic

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import com.home.hipstabot.util.HtmlResponseParser
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service


private const val YANDEX_SEARCH_URL = "https://music.yandex.ru/search?type=tracks&text="

@Service
class YandexMusicMatcher : Matcher {


    override fun getMedia(query: String): Media? {
        val request = HttpGet(query)
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = execute.entity.content.bufferedReader().readText()

        val htmlResponseParser = HtmlResponseParser(response, Document::head, "script", "class", "nonce")
        val jsonElement = htmlResponseParser.extractElements("light-data").firstOrNull() ?: return null
        val yandexTrack = ObjectMapper().readValue(jsonElement.data(), YandexTrack::class.java)


        val media = Media()
        media.link = query
        media.title = yandexTrack.name
        media.artist = yandexTrack.inAlbum.byArtist.name
        media.thumbnailUri = yandexTrack.inAlbum.image
        media.type = service()

        return media
    }

    override fun getMedia(media: Media): Media? {
        if (media.type == service()) return media

        val request = HttpGet("$YANDEX_SEARCH_URL${media.getTags().joinToString("+")}")
        val execute = HttpClientBuilder.create().build().execute(request)
        val responseContent = execute.entity.content.bufferedReader().readText()

        val htmlResponseParser = HtmlResponseParser(responseContent, Document::body, "a", "class", "href")
        val trackLink = htmlResponseParser.getExtractedValues("share-container").firstOrNull() ?: return null

        return getMedia(trackLink)
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.YANDEX_MUSIC
    }

    override fun matchesUri(uri: String): Boolean {
        return uri.contains("music.yandex.ru")
    }
}