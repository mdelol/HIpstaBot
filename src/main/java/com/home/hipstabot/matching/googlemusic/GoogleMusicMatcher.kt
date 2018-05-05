package com.home.hipstabot.matching.googlemusic

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import com.home.hipstabot.util.HtmlResponseParser
import com.home.hipstabot.util.TagUtil
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

private const val GOOGLE_SONG_URL = "https://play.google.com/music/m/"
private const val GOOGLE_SEARCH_URL = "https://play.google.com/store/search?c=music&q="
private const val GOOGLE_PAGE_REGEX_EXP = "\"song-([\\w]+)\""
private const val GOOGLE_NOT_FOUND_TITLE = "Listen on Google Play Music"
private const val TAG = "meta"
private const val POINTING_ATTRIBUTE_KEY = "property"
private const val CONTENT_ATTRIBUTE_KEY = "content"

@Service
class GoogleMusicMatcher : Matcher {

    private val googlePageRegex = GOOGLE_PAGE_REGEX_EXP.toRegex()

    override fun getMedia(query: String): Media? {

        val request = HttpGet(query)
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = execute.entity.content.bufferedReader().readText()

        val responseParser = HtmlResponseParser(response, Document::head, TAG, POINTING_ATTRIBUTE_KEY, CONTENT_ATTRIBUTE_KEY)
        val titleExtractedValues = responseParser.getExtractedValues("og:title")
        val trackInfoMetaTagValue = titleExtractedValues.firstOrNull()?: return null
        if(titleExtractedValues.firstOrNull().equals(GOOGLE_NOT_FOUND_TITLE)) return null
        val imageExtractedValues = responseParser.getExtractedValues("og:image")
        val imageMetaTagValue = imageExtractedValues.firstOrNull()?: return null
        val media = Media()
        media.type = service()
        media.setTags(TagUtil.splitTags(trackInfoMetaTagValue))
        media.link = query
        media.thumbnailUri = imageMetaTagValue
        media.setDisplayName(trackInfoMetaTagValue)

        return media
    }

    override fun getMedia(media: Media): Media? {
        if (media.type == service()) return media

        val request = HttpGet("$GOOGLE_SEARCH_URL${media.getTags().joinToString("+")}")
        val execute = HttpClientBuilder.create().build().execute(request)
        val content = execute.entity.content.bufferedReader().readText()

        val matchResult = googlePageRegex.find(content) ?: return null

        if (matchResult.groupValues.isEmpty()) return null

        val songId = matchResult.groupValues[1]
        val googleUrl = "$GOOGLE_SONG_URL$songId"

        return getMedia(googleUrl)
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.GOOGLE_MUSIC
    }

    override fun matchesUri(uri: String): Boolean {
        return uri.contains(GOOGLE_SONG_URL)
    }
}