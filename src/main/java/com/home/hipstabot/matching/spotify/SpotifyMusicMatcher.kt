package com.home.hipstabot.matching.spotify

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import com.home.hipstabot.util.HtmlResponseParser
import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.enums.ModelObjectType
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*
import kotlin.concurrent.timerTask

@Service
class SpotifyMusicMatcher : Matcher {

    @Value("\${spotify.clientId}")
    private lateinit var clientId: String
    @Value("\${spotify.clientSecret}")
    private lateinit var clientSecret: String

    private lateinit var spotifyApi: SpotifyApi

    private var timer = Timer()

    override fun getMedia(query: String): Media? {

        val request = HttpGet(query)
        val execute = HttpClientBuilder.create().build().execute(request)
        val response = execute.entity.content.bufferedReader().readText()

        val htmlResponseParser = HtmlResponseParser(response, Document::head, "meta", "property", "content")
        val song = htmlResponseParser.extractElements("twitter:title").firstOrNull() ?: return null
        val artist = htmlResponseParser.extractElements("twitter:audio:artist_name").firstOrNull() ?: return null
        val image = htmlResponseParser.extractElements("twitter:image").firstOrNull() ?: return null

        val media = Media()
        media.link = query
        media.title = song.attr("content")
        media.artist = artist.attr("content")
        media.thumbnailUri = image.attr("content")
        media.type = service()

        return media
    }

    override fun getMedia(media: Media): Media? {
        if (media.type == service()) return media
        val request = getApi().searchItem(media.getTags().joinToString("+"), ModelObjectType.TRACK.type).limit(1).build()
        val result = request.execute()
        val tracks = result.tracks
        if (tracks.items.isEmpty()) {
            return null
        }
        val firstFound = tracks.items.first()

        val resultMedia = Media()
        resultMedia.link = firstFound.externalUrls.get("spotify")
        resultMedia.title = firstFound.name
        resultMedia.artist = firstFound.artists.first().name
        resultMedia.thumbnailUri = firstFound.album.images.first().url
        resultMedia.type = service()
        return resultMedia
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.SPOTIFY_MUSIC
    }

    override fun matchesUri(uri: String): Boolean {
        return uri.contains("spotify.com")
    }

    private fun getApi(): SpotifyApi {
        if (!this::spotifyApi.isInitialized) {
            spotifyApi = SpotifyApi.builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(URI("localhost"))
                    .build()
            tickAccessToken()
        }
        return spotifyApi
    }

    private fun tickAccessToken() {
        val clientCredentialsRequest = spotifyApi.clientCredentials()
                .build()
        val clientCredentials = clientCredentialsRequest.execute()
        spotifyApi.setAccessToken(clientCredentials.getAccessToken())
        System.out.println("Token expires in: " + clientCredentials.getExpiresIn())
        timer.schedule(timerTask { tickAccessToken() }, ((clientCredentials.expiresIn - 10) * 1000L))
    }
}