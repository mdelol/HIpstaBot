package com.home.hipstabot.matching.applemusic

import com.fasterxml.jackson.databind.ObjectMapper
import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.stereotype.Service

@Service
class AppleMusicMatcher : Matcher {
    override fun getMedia(query: String): Media {
        var media = Media()

        return media
    }

    override fun getLink(media: Media): String {
        if (media.type == service()) return media.sourceLink

        val params = listOf(media.artist, media.album, media.title).filter { s -> !s.isEmpty() }.joinToString("+")

        val request = HttpGet("https://itunes.apple.com/search?limit=1&term=$params")

        val execute = HttpClientBuilder.create().build().execute(request)

        val response = ObjectMapper().readValue(execute.entity.content, ItunesResponse::class.java)

        return response.results.get(0).trackViewUrl
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.APPLE_MUSIC
    }

    override fun matchesUri(uri: String): Boolean {
        return uri.contains("itunes.apple.com")
    }
}