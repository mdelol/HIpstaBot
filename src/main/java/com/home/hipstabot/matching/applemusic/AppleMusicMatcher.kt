package com.home.hipstabot.matching.applemusic

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.stereotype.Service

@Service
class AppleMusicMatcher : Matcher {

    override fun getMedia(query: String): Media? {
        val id = URIBuilder(query).queryParams.find { qp -> qp.name == "i" }?.value
        val request = HttpGet("https://itunes.apple.com/search?limit=1&term=$id")
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = ObjectMapper().readValue(execute.entity.content, ItunesResponse::class.java)
        return convertToMedia(response.results.firstOrNull())
    }

    override fun getMedia(media: Media): Media? {
        if (media.type == service()) return media
        val params = media.getTags().joinToString("+")
        val request = HttpGet("https://itunes.apple.com/search?entity=musicTrack&limit=1&term=$params")
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = ObjectMapper().readValue(execute.entity.content, ItunesResponse::class.java)
        val result = response.results.firstOrNull()
        return convertToMedia(result)
    }

    private fun convertToMedia(result: ItunesEntity?): Media? {
        if (result == null) return null
        val resultMedia = Media()
        resultMedia.type = service()
        resultMedia.title = result.trackName
        resultMedia.artist = result.artistName
        resultMedia.link = result.trackViewUrl
        resultMedia.thumbnailUri = result.artworkUrl60
        return resultMedia
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.APPLE_MUSIC
    }

    override fun matchesUri(uri: String): Boolean {
        return uri.contains("itunes.apple.com")
    }
}