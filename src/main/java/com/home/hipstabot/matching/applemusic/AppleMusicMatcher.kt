package com.home.hipstabot.matching.applemusic

import com.home.hipstabot.matching.Matcher
import com.home.hipstabot.matching.Media
import org.springframework.stereotype.Service

@Service
class AppleMusicMatcher : Matcher {
    override fun matchesUri(Url: String): Boolean {
        return true;
    }

    override fun getMedia(query: String): Media {
        var media = Media()
        media.artist = "Adele"
        media.album = "25"
        media.sourceLink = "https://itunes.apple.com/us/album/hello/1051394208?i=1051394215&uo=4"
        media.title = "Hello"
        media.type = service()

        return media
    }

    override fun getLink(media: Media): String {
        if (media.type == service()) return media.sourceLink
        return ""
    }

    override fun service(): Media.ServiceType {
        return Media.ServiceType.APPLE_MUSIC
    }
}