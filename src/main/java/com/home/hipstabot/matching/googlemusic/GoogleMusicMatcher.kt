package com.home.hipstabot.matching.googlemusic

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.jsoup.Jsoup
import org.springframework.stereotype.Service

@Service
class GoogleMusicMatcher : Matcher {
    private val GOOGLE_SONG_URL: String = "https://play.google.com/music/m/"
    private val GOOGLE_SEARCH_URL = "https://play.google.com/store/search?c=music&q="
    private val GOOGLE_PAGE_REGEX = Regex("\"song-([\\w]+)\"")
    private val GOOGLE_NOT_FOUND_TITLE = "Listen on Google Play Music"

    override fun getMedia(query: String): Media? {

        val request = HttpGet(query)
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = execute.entity.content.bufferedReader().readText()

        //todo make a separate parser class

        val parsedResponse = Jsoup.parse(response)
        val metaTags = parsedResponse.head().getElementsByTag("meta")
        val trackInfoMetaTagValue = metaTags.filter { x -> x.attr("property") != null
                && x.attr("property").equals("og:title") }.map { x->x.attr("content") }.firstOrNull()
                    ?: return null

        if(trackInfoMetaTagValue.equals(GOOGLE_NOT_FOUND_TITLE)) return null

        val imageMetaTagValue = metaTags.filter { x -> x.attr("property") != null
                && x.attr("property").equals("og:image") }.map { x->x.attr("content") }.firstOrNull()
                ?: return null

        val media = Media()
        media.type = service()
        media.setTags(trackInfoMetaTagValue.split("[^а-яА-Я\\w]".toRegex()).filter { x->!x.isBlank() })
        media.link = query
        media.thumbnailUri = imageMetaTagValue
        media.setDisplayName(trackInfoMetaTagValue)

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