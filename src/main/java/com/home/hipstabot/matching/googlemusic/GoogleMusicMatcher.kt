package com.home.hipstabot.matching.googlemusic

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.stereotype.Service

@Service
class GoogleMusicMatcher : Matcher {
    private val GOOGLE_SONG_URL: String = "https://play.google.com/music/m/"
    private val GOOGLE_SEARCH_URL = "https://play.google.com/store/search?c=music&q="
    private val GOOGLE_PAGE_REGEX = Regex("\"song-([\\w]+)\"")
    private val GOOGLE_RESPONSE_TRACK_INFO_REGEX = Regex("<meta property=\"og:title\" content=\"(.+?)\"/>")
    private val GOOGLE_RESPONSE_THUMBNAIL_REGEX = Regex("<meta property=\"og:image\" content='(\\w+:/?/?[^\\s]+)?'/>")

    override fun getMedia(query: String): Media? {

        val request = HttpGet(query)
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = execute.entity.content.bufferedReader().readText()

        val trackInfoMatchResult = GOOGLE_RESPONSE_TRACK_INFO_REGEX.find(response) ?: return null
        val trackInfo = trackInfoMatchResult.groupValues.last()

        val thumbnailMatchResult = GOOGLE_RESPONSE_THUMBNAIL_REGEX.find(response) ?: return null
        val thumbnail = thumbnailMatchResult.groupValues.last()
        val media = Media()
        media.type = service()
        media.setTags(trackInfo.split("[^а-яА-Я\\w]".toRegex()))
        media.link = query
        media.thumbnailUri = thumbnail
        media.setDisplayName(trackInfo)
        //todo add thumbnail
        return media;
    }

    override fun getMedia(media: Media): Media? {
        if (media.type == service()) return media

        val request = HttpGet("${GOOGLE_SEARCH_URL}${media.getTags().joinToString("+")}")
        val execute = HttpClientBuilder.create().build().execute(request)
        val content = execute.entity.content.bufferedReader().readText()

        val matchResult = GOOGLE_PAGE_REGEX.find(content) ?: return null

        if (matchResult.groupValues.isEmpty()) return null;

        val songId = matchResult.groupValues[1]
        val googleUrl = "${GOOGLE_SONG_URL}${songId}"

        return getMedia(googleUrl)
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.GOOGLE_MUSIC;
    }

    override fun matchesUri(uri: String): Boolean {
        return uri.contains(GOOGLE_SONG_URL)
    }
}